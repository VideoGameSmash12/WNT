package me.videogamesm12.wnt.supervisor.mixin.render;

import me.videogamesm12.wnt.supervisor.networking.NetworkStorage;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.networking.PacketType;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
    /*@Inject(method = "onBlockUpdate", at = @At("HEAD"))
    public void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_PLAY_BLOCK_UPDATE)
                .cancelled(ci.isCancelled())
                .build()
                .invokeEvent();
    }*/

    @Inject(method = "onChunkData", at = @At("HEAD"))
    public void onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_PLAY_CHUNK_DATA)
                .cancelled(ci.isCancelled())
                .build()
                .invokeEvent();
    }

    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci)
    {
        if (Supervisor.CONFIG.network().ignoreEntitySpawns())
        {
            ci.cancel();
        }

        /*NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_PLAY_ENTITY_SPAWN)
                .cancelled(ci.isCancelled())
                .build()
                .invokeEvent();*/
    }

    @Inject(method = "onExplosion", at = @At("HEAD"), cancellable = true)
    public void onExplosion(ExplosionS2CPacket packet, CallbackInfo ci)
    {
        if (Supervisor.CONFIG.network().ignoreExplosions())
        {
            ci.cancel();
        }

        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_PLAY_EXPLOSION)
                .cancelled(ci.isCancelled())
                .build()
                .invokeEvent();
    }

    @Inject(method = "onLightUpdate", at = @At("HEAD"), cancellable = true)
    public void onLightUpdate(LightUpdateS2CPacket packet, CallbackInfo ci)
    {
        if (Supervisor.CONFIG.network().ignoreLightUpdates())
        {
            ci.cancel();
        }

        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_PLAY_LIGHT_UPDATE)
                .cancelled(ci.isCancelled())
                .build()
                .invokeEvent();
    }

    @Inject(method = "onUnloadChunk", at = @At("HEAD"), cancellable = true)
    public void onUnloadChunk(UnloadChunkS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_PLAY_UNLOAD_CHUNK)
                .cancelled(ci.isCancelled())
                .build()
                .invokeEvent();
    }
}
