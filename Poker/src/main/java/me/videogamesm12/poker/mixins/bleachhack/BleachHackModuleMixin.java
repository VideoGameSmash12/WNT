package me.videogamesm12.poker.mixins.bleachhack;

import me.videogamesm12.poker.Poker;
import me.videogamesm12.poker.core.event.ModuleToggleEvent;
import net.minecraft.util.Identifier;
import org.bleachhack.module.Module;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Module.class)
public class BleachHackModuleMixin
{
    @Inject(method = "setEnabled", at = @At("TAIL"), remap = false)
    public void onModuleToggled(boolean enabled, CallbackInfo ci)
    {
        Poker.getHouse(new Identifier("poker", "bleachhack")).post(new ModuleToggleEvent<>(this, enabled));
    }
}
