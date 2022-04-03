package me.videogamesm12.wnt.supervisor.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientWorld.class)
public interface ClientWorldMixin
{
    @Accessor("entityList")
    EntityList getEntityList();
}