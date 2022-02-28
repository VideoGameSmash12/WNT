package me.videogamesm12.w95.supervisor.mixin.render;

import me.videogamesm12.w95.supervisor.Supervisor;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin
{
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void startRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline,
        Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f,
        CallbackInfo ci)
    {
        // Don't render the world if it's disabled by the Supervisor.
        if (Supervisor.SupervisorModule.CONFIG.rendering().disableWorldRendering()
                || Supervisor.SupervisorModule.CONFIG.rendering().disableGameRendering())
        {
            ci.cancel();
        }
    }

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    public void startRenderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci)
    {
        // Don't render any weather if it's disabled.
        if (Supervisor.SupervisorModule.CONFIG.rendering().disableWeatherRendering()
                || Supervisor.SupervisorModule.CONFIG.rendering().disableGameRendering())
        {
            ci.cancel();
        }
    }

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void startRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
        MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci)
    {
        // Don't render entities if disabled.
        if (Supervisor.SupervisorModule.CONFIG.rendering().disableEntityRendering()
                || Supervisor.SupervisorModule.CONFIG.rendering().disableGameRendering())
        {
            ci.cancel();
        }
    }
}
