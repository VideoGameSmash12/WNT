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
import net.minecraft.text.StringVisitable;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * <h1>BoundlessTranslation</h1>
 * Patches an exploit caused by insufficient boundary checks in the translation system
 */
@CPatch(name = "BoundlessTranslation", description = "No description")
@Mixin(TranslatableText.class)
public class BoundlessTranslation
{
    @Shadow @Final private static StringVisitable NULL_ARGUMENT;

    @Inject(method = "getArg", at = @At("HEAD"), cancellable = true)
    public void fixCrashExploit(int index, CallbackInfoReturnable<StringVisitable> cir)
    {
        if (CFX.getConfig().getCompPatches().getTranslatePatches().isBoundaryPatchEnabled() && index < 0)
        {
            cir.setReturnValue(NULL_ARGUMENT);
        }
    }
}
