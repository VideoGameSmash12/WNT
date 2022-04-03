package me.videogamesm12.wnt.supervisor.mixin.render;

import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin
{
    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
    public <E extends BlockEntity> void injectRenderEntity(E blockEntity, float tickDelta, MatrixStack matrix,
        VertexConsumerProvider vertexConsumerProvider, CallbackInfo ci)
    {
        if (Supervisor.SupervisorModule.CONFIG.rendering().disableTileEntityRendering()
                || Supervisor.SupervisorModule.CONFIG.rendering().disableGameRendering())
        {
            ci.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
    private static <E extends BlockEntity> void injectRenderEntityRenderer(BlockEntityRenderer<E> renderer,
        E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci)
    {
        if (Supervisor.SupervisorModule.CONFIG.rendering().disableTileEntityRendering()
                || Supervisor.SupervisorModule.CONFIG.rendering().disableGameRendering())
        {
            ci.cancel();
        }
    }
}
