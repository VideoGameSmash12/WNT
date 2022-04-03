package me.videogamesm12.wnt.supervisor.mixin.gui;

import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudMixin
{
    @Accessor
    DebugHud getDebugHud();
}
