package me.videogamesm12.w95.supervisor.mixin.render;

import me.videogamesm12.w95.supervisor.Supervisor;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void startRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci)
    {
        // Refuses to render anything period.
        if (Supervisor.SupervisorModule.CONFIG.rendering().disableGameRendering())
        {
            ci.cancel();
        }
    }
}
