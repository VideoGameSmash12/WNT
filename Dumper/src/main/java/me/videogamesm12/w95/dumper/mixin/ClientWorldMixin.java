package me.videogamesm12.w95.dumper.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.map.MapState;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ClientWorld.class)
public interface ClientWorldMixin
{
    @Invoker("getEntityLookup")
    EntityLookup<Entity> getEntityLookup();

    @Accessor("mapStates")
    Map<String, MapState> getMapStates();
}