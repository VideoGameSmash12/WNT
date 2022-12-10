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

package me.videogamesm12.wnt.cfx.patches;

import me.videogamesm12.wnt.cfx.CFX;
import me.videogamesm12.wnt.cfx.base.CPatch;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

public class ExcessivelyLongText
{
    @CPatch(name = "wnt.cfx.excessively_long_text.entities", description = "wnt.cfx.excessively_long_text.entities.desc")
    @Mixin(EntityRenderer.class)
    public static class EntityRendering
    {
        @ModifyVariable(method = "renderLabelIfPresent", at = @At(value = "HEAD"), argsOnly = true)
        private Text limitLabelSize(Text text)
        {
            if (CFX.getConfig().getRendererPatches().getEntities().isEntityNameSizeLimitEnabled()
                    && text.getString().length() > CFX.getConfig().getRendererPatches().getEntities().getEntityNameSizeLimit())
            {
                return MutableText.of(new LiteralTextContent(text.asTruncatedString(CFX.getConfig().getRendererPatches().getEntities().getEntityNameSizeLimit())));
            }

            return text;
        }
    }
}
