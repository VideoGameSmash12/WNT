/*
 * Copyright (c) 2022 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.videogamesm12.wnt.cfx;

import me.videogamesm12.wnt.cfx.base.CPatch;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <h1>PatchManager</h1>
 * Manages most of the patches CFX provides, primarily by determining whether not a patch should be applied.
 */
public class PatchManager implements IMixinConfigPlugin
{
    private static final String packaqe = "me.videogamesm12.wnt.cfx.patches";
    //--
    private final FabricLoader loader = FabricLoader.getInstance();

    @Override
    public void onLoad(String mixinPackage)
    {
        CFX.getLogger().info("Setting up patches...");
    }

    @Override
    public String getRefMapperConfig()
    {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        // Ignore non-patch mixins
        if (!mixinClassName.startsWith(packaqe))
        {
            return true;
        }

        try
        {
            final Class<?> patchClass = Class.forName(mixinClassName);

            // WTF?
            if (!patchClass.isAnnotationPresent(CPatch.class))
            {
                CFX.getLogger().warn("Ignoring patch class " + mixinClassName + " as it is missing the CPatch annotation");
                CFX.getLogger().warn("This should normally never show up in a non-development environment, so if you're seeing this in such an environment, open an issue on GitHub immediately!");
                return false;
            }

            // Gets the patch metadata
            final CPatch metadata = patchClass.getAnnotation(CPatch.class);

            // Ignore patches if mods they conflict with are loaded
            for (String id : metadata.conflicts())
            {
                if (loader.isModLoaded(id))
                {
                    CFX.getLogger().warn("Ignoring patch " + mixinClassName + " as it conflicts with mod '" + id + "'");
                    return false;
                }
            }

            // Ignore patches if they are not compatible with the version of Minecraft that is currently running
            for (String version : metadata.supportedVersions())
            {
                final Optional<ModContainer> minecraftContainer = loader.getModContainer("minecraft");

                if (minecraftContainer.isEmpty())
                {
                    CFX.getLogger().error("Ignoring patch " + mixinClassName + " as the Minecraft mod container doesn't exist. This should never happen unless something has gone seriously wrong somewhere down the line.");
                    return false;
                }

                // We're running a newer version of the game that doesn't support this patch. Ignore it.
                if (minecraftContainer.get().getMetadata().getVersion() instanceof SemanticVersion sem && sem.compareTo(Version.parse(version)) < 0)
                {
                    CFX.getLogger().warn("Ignoring patch " + mixinClassName + " as it doesn't support this version of Minecraft (patch only supports versions " + version + ", we are running on Minecraft " + sem.getFriendlyString() + ")");
                    return false;
                }
            }
        }
        catch (Exception ignored)
        {
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
    {
    }

    @Override
    public List<String> getMixins()
    {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {
    }
}
