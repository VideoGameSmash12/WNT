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

package me.videogamesm12.wnt;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WNT implements ModInitializer, ClientLifecycleEvents.ClientStopping
{
    @Getter
    private static WConfig config = null;
    @Getter
    private static final EventBus eventBus = new EventBus();
    @Getter
    private static final Logger logger = LogManager.getLogger("WNT");
    @Getter
    private static ModuleManager moduleManager;

    @Override
    public void onInitialize()
    {
        loadConfig();
        //--
        moduleManager = new ModuleManager();
        moduleManager.loadModules();
        //--
        ClientLifecycleEvents.CLIENT_STOPPING.register(this);
    }

    @Override
    public void onClientStopping(MinecraftClient client)
    {
        saveConfig();
        moduleManager.stopAll();
    }

    public void loadConfig()
    {
        File file = new File(WNT.getWNTFolder(), "wnt.json");
        if (file.exists())
        {
            try
            {
                config = new Gson().fromJson(new FileReader(file), WConfig.class);
                return;
            }
            catch (Exception ex)
            {
                logger.error("Failed to read WNT configuration", ex);
            }
        }

        config = new WConfig();
    }

    public void saveConfig()
    {
        config.setEnabledModules();
        //--
        File file = new File(WNT.getWNTFolder(), "wnt.json");
        try (FileWriter writer = new FileWriter(file))
        {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
        }
        catch (Exception ex)
        {
            logger.error("Failed to write WNT configuration", ex);
        }
    }

    public static File getWNTFolder()
    {
        File file = new File(MinecraftClient.getInstance().runDirectory, "wnt");

        if (!file.isDirectory())
        {
            file.mkdirs();
        }

        return file;
    }

    public static File getWNTFolderSafe()
    {
        File file = new File(FabricLoader.getInstance().getGameDir().toFile(), "wnt");

        if (!file.isDirectory())
        {
            file.mkdirs();
        }

        return file;
    }

    public static class WConfig
    {
        @Getter
        private List<String> enabledModules = new ArrayList<>();

        public void setEnabledModules()
        {
            enabledModules.clear();

            moduleManager.getModules().forEach((key, value) ->
            {
                if (value.isEnabled() && !enabledModules.contains(key.getName()))
                {
                    enabledModules.add(key.getName());
                }
            });
        }

        public <T extends Module> boolean isEnabled(Class<T> moduleClass)
        {
            return enabledModules.contains(moduleClass.getName());
        }
    }
}
