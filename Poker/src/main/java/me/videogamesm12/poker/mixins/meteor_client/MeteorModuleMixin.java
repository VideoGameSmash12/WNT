package me.videogamesm12.poker.mixins.meteor_client;

import me.videogamesm12.poker.Poker;
import me.videogamesm12.poker.core.event.ModuleToggleEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Module.class)
public abstract class MeteorModuleMixin
{
    @Shadow public abstract boolean isActive();

    @Inject(method = "toggle", at = @At("TAIL"), remap = false)
    public void injectToggle(CallbackInfo ci)
    {
        // Calls the event
        Poker.getHouse(new Identifier("poker", "meteor")).post(new ModuleToggleEvent<>(this, isActive()));
    }
}
