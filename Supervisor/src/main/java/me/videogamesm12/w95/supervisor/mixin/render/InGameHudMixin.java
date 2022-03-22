package me.videogamesm12.w95.supervisor.mixin.render;

import me.videogamesm12.w95.supervisor.event.HUDMessageAdded;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
    @Inject(method = "addChatMessage", at = @At("RETURN"))
    public void addMessage(MessageType type, Text message, UUID sender, CallbackInfo ci)
    {
        HUDMessageAdded.EVENT.invoker().onMessageAdded(type, message, sender);
    }
}
