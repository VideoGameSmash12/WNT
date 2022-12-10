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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.videogamesm12.wnt.cfx.CFX;
import me.videogamesm12.wnt.cfx.base.CPatch;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * <h1>HoverableText</h1>
 * Patches exploits in hoverable text components, including ones that can crash clients trying to process them.
 */
public class HoverableText
{
    @CPatch(name = "wnt.cfx.hover_events.entities", description = "wnt.cfx.hover_events.entities.desc")
    @Mixin(HoverEvent.EntityContent.class)
    public static class Entities
    {
        /**
         * Fixes an exploit caused by invalid identifiers in the "show_entity" hover event.
         * @param json  JsonElement
         * @param cir   CallbackInfoReturnable
         */
        @Inject(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$EntityContent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", shift = At.Shift.BEFORE), cancellable = true)
        private static void patchIdentifierJson(JsonElement json, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
        {
            if (CFX.getConfig().getCompPatches().getHoverPatches().isIdPatchEnabled())
            {
                JsonObject object = json.getAsJsonObject();

                if (JsonHelper.hasString(object, "type") && !Identifier.isValid(JsonHelper.getString(json.getAsJsonObject(), "type")))
                {
                    cir.setReturnValue(null);
                }
            }
        }

        /**
         * Fixes an exploit caused by invalid identifiers in the "show_entity" hover event.
         * @param text  Text
         * @param cir   CallbackInfoReturnable
         */
        @Inject(method = "parse(Lnet/minecraft/text/Text;)Lnet/minecraft/text/HoverEvent$EntityContent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", shift = At.Shift.BEFORE), cancellable = true)
        private static void patchIdentifierText(Text text, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
        {
            if (CFX.getConfig().getCompPatches().getHoverPatches().isIdPatchEnabled())
            {
                try
                {
                    NbtCompound nbt = StringNbtReader.parse(text.getString());
                    if (!Identifier.isValid(nbt.getString("type")))
                    {
                        cir.setReturnValue(null);
                    }
                }
                catch (CommandSyntaxException ignored)
                {
                    cir.setReturnValue(null);
                }
            }
        }
    }

    @CPatch(name = "wnt.cfx.hover_events.items", description = "wnt.cfx.hover_events.items.desc")
    @Mixin(HoverEvent.ItemStackContent.class)
    public static class Items
    {
        /**
         * Fixes an exploit caused by invalid identifiers in the "show_item" hover event.
         * @param id    String
         * @return      String
         */
        @ModifyArg(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$ItemStackContent;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;<init>(Ljava/lang/String;)V"))
        private static String validate(String id)
        {
            if (!CFX.getConfig().getCompPatches().getHoverPatches().isIdPatchEnabled())
                return id;

            return Identifier.isValid(id) ? id : "minecraft:air";
        }
    }
}
