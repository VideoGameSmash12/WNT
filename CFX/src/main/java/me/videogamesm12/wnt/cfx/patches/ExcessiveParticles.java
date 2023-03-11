package me.videogamesm12.wnt.cfx.patches;

import me.videogamesm12.wnt.cfx.CFX;
import me.videogamesm12.wnt.cfx.base.CPatch;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * <h1>ExcessiveParticles</h1>
 * <p>Patches exploits caused by excessive particles.</p>
 */
@CPatch(name = "wnt.cfx.packet.particle", description = "wnt.cfx.packet.particle.desc")
@Mixin(ClientPlayNetworkHandler.class)
public class ExcessiveParticles
{
    /**
     * Fixes an exploit caused by particle packets with extreme counts.
     * @param packet    ParticleS2CPacket
     * @param ci        CallbackInfo
     */
    @Inject(method = "onParticle", at = @At("HEAD"), cancellable = true)
    public void onParticleSpawn(ParticleS2CPacket packet, CallbackInfo ci)
    {
        if (CFX.getConfig().getPacketPatches().isParticleLimitEnabled()
                && packet.getCount() > CFX.getConfig().getPacketPatches().getParticleLimit())
        {
            ci.cancel();
        }
    }
}
