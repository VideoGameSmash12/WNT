package me.videogamesm12.wnt.supervisor.mixin.render;

import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci)
    {
        if (Supervisor.CONFIG.network().ignoreEntitySpawns())
        {
            ci.cancel();
        }
    }

    @Inject(method = "onExplosion", at = @At("HEAD"), cancellable = true)
    public void onExplosion(ExplosionS2CPacket packet, CallbackInfo ci)
    {
        if (Supervisor.CONFIG.network().ignoreExplosions())
        {
            ci.cancel();
        }
    }

    @Inject(method = "onLightUpdate", at = @At("HEAD"), cancellable = true)
    public void onLightUpdate(LightUpdateS2CPacket packet, CallbackInfo ci)
    {
        if (Supervisor.CONFIG.network().ignoreLightUpdates())
        {
            ci.cancel();
        }
    }
}
