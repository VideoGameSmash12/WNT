package me.videogamesm12.wnt.supervisor.mixin.render;

import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    /**
     * <p>Disables rendering if the configuration says to not render anything period.</p>
     * @param tickDelta float
     * @param startTime long
     * @param tick      boolean
     * @param ci        CallbackInfo
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void startRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci)
    {
        // Refuses to render anything period.
        if (Supervisor.CONFIG.rendering().disableGameRendering())
        {
            ci.cancel();
        }
    }
}
