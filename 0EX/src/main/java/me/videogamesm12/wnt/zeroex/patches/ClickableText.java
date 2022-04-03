package me.videogamesm12.wnt.zeroex.patches;

import me.videogamesm12.wnt.zeroex.ZeroEx;
import me.videogamesm12.wnt.zeroex.ZeroExConfig;
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

@Mixin(Screen.class)
public class ClickableText
{
    /**
     * Disables the "run_command" click event.
     * @param style Style
     * @param cir   CallbackInfoReturnable<Boolean>
     */
    @Inject(method = "handleTextClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;sendMessage(Ljava/lang/String;Z)V"), cancellable = true)
    public void disableRunCommand(Style style, CallbackInfoReturnable<Boolean> cir)
    {
        ZeroExConfig.ZText.ClickEvents.RCPatchMethod patchMethod = ZeroEx.CONFIG.getText().clickEvents().getRunCommandFix();

        switch (patchMethod)
        {
            case SILENT -> cir.setReturnValue(true);
            case PROMPT -> {
                // WTF?
                if (style.getClickEvent() == null)
                {
                    return;
                }

                cir.setReturnValue(false);

                MinecraftClient.getInstance().setScreen(new ConfirmScreen((result) -> {
                        if (result)
                        {
                            MinecraftClient.getInstance().player.sendChatMessage(style.getClickEvent().getValue());
                        }

                        MinecraftClient.getInstance().setScreen(null);
                    },
                    new TranslatableText("wnt.messages.0ex.rcprompt.title"),
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
        if (ZeroEx.CONFIG.getText().clickEvents().isScfEnabled())
        {
            String text = args.get(0);
            //--
            args.set(0, text.length() > 256 ? text.substring(0, 255) : text);
        }
    }
}
