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

package me.videogamesm12.wnt.zeroex.patches;

import com.google.gson.JsonElement;
import me.videogamesm12.wnt.zeroex.ZeroEx;
import me.videogamesm12.wnt.zeroex.ZeroExConfig;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

/**
 * <h1>HoverUUID</h1>
 * Patches an exploit that causes clients to crash trying to process malicious text.
 */
@Mixin(HoverEvent.EntityContent.class)
public class HoverUUID
{
    /**
     * Silently patches the invalid UUID text exploit if the method selected is set to SILENT.
     * @param json  JsonElement
     * @param cir   CallbackInfoReturnable<HoverEvent.EntityContent> cir
     */
    @Inject(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$EntityContent;",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;",
            shift = At.Shift.AFTER),
        cancellable = true)
    private static void injectParseJson(JsonElement json, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
    {
        if (ZeroEx.CONFIG.getText().hoverEvents().getBeuFix() == ZeroExConfig.ZText.HoverEvents.BEUPatchMethod.SILENT)
        {
            try
            {
                UUID.fromString(JsonHelper.getString(json.getAsJsonObject(), "id"));
            }
            catch (Exception ex)
            {
                cir.setReturnValue(null);
            }
        }
    }

    /**
     * Silently patches the invalid UUID text exploit if the method selected is set to SILENT.
     * @param text  Text
     * @param cir   CallbackInfoReturnable<HoverEvent.EntityContent> cir
     */
    @Inject(method = "parse(Lnet/minecraft/text/Text;)Lnet/minecraft/text/HoverEvent$EntityContent;",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/registry/DefaultedRegistry;get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;",
            shift = At.Shift.AFTER),
        cancellable = true)
    private static void injectParseText(Text text, CallbackInfoReturnable<HoverEvent.EntityContent> cir)
    {
        if (ZeroEx.CONFIG.getText().hoverEvents().getBeuFix() == ZeroExConfig.ZText.HoverEvents.BEUPatchMethod.SILENT)
        {
            try
            {
                UUID.fromString(StringNbtReader.parse(text.getString()).getString("id"));
            }
            catch (Exception ex)
            {
                cir.setReturnValue(null);
            }
        }
    }

    /**
     * Replaces invalid UUIDs passed to fromString with a valid UUID if the method selected is set to VISIBLE.
     * @param uuid  String
     */
    @ModifyArg(method = "parse(Lnet/minecraft/text/Text;)Lnet/minecraft/text/HoverEvent$EntityContent;",
            at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;"))
    private static String modifyUuidText(String uuid)
    {
        if (ZeroEx.CONFIG.getText().hoverEvents().getBeuFix() == ZeroExConfig.ZText.HoverEvents.BEUPatchMethod.VISIBLE)
        {
            try
            {
                return UUID.fromString(uuid).toString();
            }
            catch (Exception ex)
            {
                return "DEADDEAD-DEAD-DEAD-DEAD-DEADDEADDEAD";
            }
        }

        return uuid;
    }

    /**
     * Replaces invalid UUIDs passed to fromString with a valid UUID if the method selected is set to VISIBLE.
     * @param uuid  String
     */
    @ModifyArg(method = "parse(Lcom/google/gson/JsonElement;)Lnet/minecraft/text/HoverEvent$EntityContent;",
            at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;"))
    private static String modifyUuidJson(String uuid)
    {
        if (ZeroEx.CONFIG.getText().hoverEvents().getBeuFix() == ZeroExConfig.ZText.HoverEvents.BEUPatchMethod.VISIBLE)
        {
            try
            {
                return UUID.fromString(uuid).toString();
            }
            catch (Exception ex)
            {
                return "DEADDEAD-DEAD-DEAD-DEAD-DEADDEADDEAD";
            }
        }

        return uuid;
    }
}
