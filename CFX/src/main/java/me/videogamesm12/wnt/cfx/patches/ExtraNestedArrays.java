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

import com.google.gson.*;
import me.videogamesm12.wnt.cfx.CFX;
import me.videogamesm12.wnt.cfx.base.CPatch;
import me.videogamesm12.wnt.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * <h1>ExtraNestedArrays</h1>
 * Patches an exploit caused by insufficient sanitization checks in the text serialization system
 */
@CPatch(name = "wnt.cfx.extra_nested_arrays.name", description = "wnt.cfx.extra_nested_arrays.desc")
@Mixin(Text.Serializer.class)
public class ExtraNestedArrays
{
    /**
     * This method patches two exploits: empty arrays and array depths.
     * @param jsonElement                   JsonElement
     * @param type                          Type
     * @param jsonDeserializationContext    JsonDeserializationContext
     * @param cir                           CallbackInfoReturnable<MutableText>
     */
    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonElement;getAsJsonArray()Lcom/google/gson/JsonArray;", shift = At.Shift.AFTER), cancellable = true)
    public void patchArrayExploits(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<MutableText> cir)
    {
        final JsonArray array = jsonElement.getAsJsonArray();

        // Patch #1 - Handle empty arrays properly
        if (array.isEmpty() || array.size() <= 0)
        {
            switch (CFX.getConfig().getCompPatches().getGeneralPatches().getDoubleArrayFix())
            {
                case OBVIOUS -> cir.setReturnValue(Messenger.convert(Component.text("*** Component array is empty ***", NamedTextColor.RED)).copy());
                case VANILLA -> throw new JsonParseException("Unexpected empty array of components");
                default -> {}
            }
        }

        // Patch #2 - Handle components with too much depth
        if (CFX.getConfig().getCompPatches().getGeneralPatches().isArrayDepthFixEnabled())
        {
            validateComponentDepth(array, 0, CFX.getConfig().getCompPatches().getGeneralPatches().getArrayDepthMaximum(), cir);
        }
    }

    /**
     * This patch prevents certain text components from being too complex.
     * @param jsonElement                   JsonElement
     * @param type                          Type
     * @param jsonDeserializationContext    JsonDeserializationContext
     * @param cir                           CallbackInfoReturnable<MutableText>
     */
    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonElement;getAsJsonObject()Lcom/google/gson/JsonObject;", shift = At.Shift.AFTER), cancellable = true)
    public void patchComponentDepth(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<MutableText> cir)
    {
        if (CFX.getConfig().getCompPatches().getGeneralPatches().isArrayDepthFixEnabled())
        {
            validateComponentDepth(jsonElement.getAsJsonObject(), 0, CFX.getConfig().getCompPatches().getGeneralPatches().getArrayDepthMaximum(), cir);
        }
    }

    public void validateComponentDepth(JsonElement e, long depth, long max, CallbackInfoReturnable<MutableText> cir)
    {
        if (depth > max)
        {
            switch (CFX.getConfig().getCompPatches().getGeneralPatches().getArrayDepthFix())
            {
                case OBVIOUS -> cir.setReturnValue(Messenger.convert(Component.text("*** Component is too complex ***", NamedTextColor.RED)).copy());
                case VANILLA -> throw new JsonParseException("Component is too complex, depth >= " + max);
            }
        }

        if (e.isJsonObject())
        {
            final JsonObject object = e.getAsJsonObject();

            if (object.has("extra") && object.get("extra").isJsonArray())
            {
                validateArrayDepth(object.getAsJsonArray("extra"), depth, max, cir);
            }
            else if (object.has("translate") && object.has("with") && object.get("with").isJsonArray())
            {
                validateArrayDepth(object.getAsJsonArray("with"), depth, max, cir);
            }
        }
        else if (e.isJsonArray())
        {
            validateArrayDepth(e.getAsJsonArray(), depth, max, cir);
        }
    }

    public void validateArrayDepth(final JsonArray array, long depth, long max, CallbackInfoReturnable<MutableText> cir)
    {
        final long depth2 = depth + 1;

        for (JsonElement element : array)
        {
            validateComponentDepth(element, depth2, max, cir);
        }
    }
}