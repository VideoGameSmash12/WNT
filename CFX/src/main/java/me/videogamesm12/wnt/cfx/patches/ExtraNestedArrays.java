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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
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

/**
 * <h1>ExtraNestedArrays</h1>
 * Patches an exploit caused by insufficient sanitization checks in the text serialization system
 */
@CPatch(name = "wnt.cfx.extra_nested_arrays.name", description = "wnt.cfx.extra_nested_arrays.desc")
@Mixin(Text.Serializer.class)
public class ExtraNestedArrays
{
    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/text/MutableText;", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonElement;getAsJsonArray()Lcom/google/gson/JsonArray;", shift = At.Shift.AFTER), cancellable = true)
    public void patchExploit(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, CallbackInfoReturnable<MutableText> cir)
    {
        JsonArray array = jsonElement.getAsJsonArray();
        if (array.isEmpty() || array.size() <= 0)
        {
            switch (CFX.getConfig().getCompPatches().getGeneralPatches().getDoubleArrayFix())
            {
                case OBVIOUS -> cir.setReturnValue(Messenger.convert(Component.text("*** Component array is empty ***", NamedTextColor.RED)).copy());
                case VANILLA -> throw new JsonParseException("Unexpected empty array of components");
                default -> {}
            }
        }
    }
}