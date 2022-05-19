/*
 * Copyright (c) 2022 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
