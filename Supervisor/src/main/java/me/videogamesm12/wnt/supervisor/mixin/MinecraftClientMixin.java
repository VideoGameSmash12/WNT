package me.videogamesm12.wnt.supervisor.mixin;

import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

/**
 * <h1>MinecraftClientMixin</h1>
 * <p>The Supervisor uses this as a point of reference to determine if a client has hanged.</p>
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    /**
     * <p>Supervisor's freeze detection works by injecting some code at the tail-end of the game's rendering method to
     *  store a timestamp for when the last time a frame successfully rendered occurs, then periodically checking
     *  through another thread if it exceeds 5 seconds.</p>
     * <p>This code is what stores the timestamps.</p>
     * @param bool  boolean
     * @param ci    CallbackInfo
     */
    @Inject(method = "render", at = @At("RETURN"))
    public void onPostRender(boolean bool, CallbackInfo ci)
    {
        Supervisor.SupervisorThread.LAST_RENDERED = Instant.now().toEpochMilli();
    }
}
