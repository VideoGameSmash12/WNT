package me.videogamesm12.wnt.supervisor.mixin.gui;

import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(DebugHud.class)
public interface DebugHudMixin
{
    @Invoker("getLeftText")
    List<String> getLeftText();
}
