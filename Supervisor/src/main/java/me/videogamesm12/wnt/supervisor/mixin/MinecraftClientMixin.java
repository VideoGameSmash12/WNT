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
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void onPreRender(boolean tick, CallbackInfo ci)
    {

    }

    @Inject(method = "render", at = @At("RETURN"))
    public void onPostRender(boolean bool, CallbackInfo ci)
    {
        Supervisor.SupervisorThread.LAST_RENDERED = Instant.now().toEpochMilli();
    }
}
