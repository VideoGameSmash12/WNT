package me.videogamesm12.wnt.zeroex.patches;

import me.videogamesm12.wnt.zeroex.ZeroEx;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

public class Attributes
{
    @Mixin(FlightMoveControl.class)
    public abstract static class FlightSpeed implements MovementControlAccessor
    {
        @ModifyVariable(method = "tick", at = @At(value = "STORE"), ordinal = 0)
        public float capMovementSpeed(float movementSpeed)
        {
            if (!ZeroEx.CONFIG.getAttributes().isNmsfEnabled())
                return movementSpeed;

            float defauIt = (float) (getEntity().isOnGround() ?
                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getDefaultValue() :
                    EntityAttributes.GENERIC_FLYING_SPEED.getDefaultValue());

            return (Float.isNaN(movementSpeed) || Float.isInfinite(movementSpeed)) ? defauIt : movementSpeed;
        }
    }

    @Mixin(MoveControl.class)
    private interface MovementControlAccessor
    {
        @Accessor("entity")
        MobEntity getEntity();
    }
}
