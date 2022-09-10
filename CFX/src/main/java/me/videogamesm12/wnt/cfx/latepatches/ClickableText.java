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

package me.videogamesm12.wnt.cfx.latepatches;

import me.videogamesm12.wnt.cfx.CFX;
import me.videogamesm12.wnt.cfx.base.CPatch;
import me.videogamesm12.wnt.cfx.config.CFXConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * <h1>ClickableText</h1>
 * Patches exploits in clickable text components, such as the widely-abused "run_command" portion.
 * @implNote This one is considered a late patch as testing has shown that it will crash the entire client if it is
 *  managed by the PatchManager and the user happens to use mods like Sodium and the Replay Mod.
 */
@CPatch(name = "wnt.cfx.click_events", description = "wnt.cfx.click_events.desc")
@Mixin(Screen.class)
public class ClickableText
{
    /**
     * Disables the "run_command" click event.
     * @param style Style
     * @param cir   CallbackInfoReturnable<Boolean>
     */
    @Inject(method = "handleTextClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;sendMessage(Ljava/lang/String;Z)V", shift = At.Shift.BEFORE), cancellable = true)
    public void disableRunCommand(Style style, CallbackInfoReturnable<Boolean> cir)
    {
        CFXConfig.TextComponents.CText.RCMode patchMethod = CFX.getConfig().getCompPatches().getClickPatches().getRcMode();

        switch (patchMethod)
        {
            case SILENT -> cir.setReturnValue(true);
            case PROMPT -> {
                // WTF?
                if (style.getClickEvent() == null)
                {
                    return;
                }

                cir.setReturnValue(true);

                MinecraftClient.getInstance().setScreen(new ConfirmScreen((result) -> {
                        if (result)
                        {
                            MinecraftClient.getInstance().player.sendChatMessage(style.getClickEvent().getValue());
                        }

                        MinecraftClient.getInstance().setScreen(null);
                    },
                    new TranslatableText("wnt.messages.cfx.rcprompt.title"),
                    new LiteralText(style.getClickEvent().getValue())
                ));
            }
            default -> {}
        }
    }

    /**
     * Limits how much text can be thrown into the insertText method.
     * @param args  Args
     */
    @ModifyArgs(method = "handleTextClick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;insertText(Ljava/lang/String;Z)V"))
    public void limitSuggestCommand(Args args)
    {
        CFXConfig.TextComponents.CText patches = CFX.getConfig().getCompPatches().getClickPatches();

        if (patches.isSuggestLimitEnabled())
        {
            String text = args.get(0);
            //--
            args.set(0, text.length() > patches.getSuggestLimit() ? text.substring(0, patches.getSuggestLimit() - 1) : text);
        }
    }
}
