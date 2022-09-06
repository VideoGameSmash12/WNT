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

package me.videogamesm12.wnt.toolbox.mixin;

import me.videogamesm12.wnt.toolbox.Toolbox;
import me.videogamesm12.wnt.toolbox.data.QueriedBlockDataSet;
import me.videogamesm12.wnt.toolbox.data.QueriedEntityDataSet;
import me.videogamesm12.wnt.toolbox.event.client.CopyBlockToClipboard;
import me.videogamesm12.wnt.toolbox.event.client.CopyEntityToClipboard;
import net.minecraft.block.BlockState;
import net.minecraft.client.Keyboard;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin
{
    @Inject(method = "copyEntity", at = @At(value = "HEAD"))
    public void injectCopyEntity(Identifier id, Vec3d pos, NbtCompound nbt, CallbackInfo ci)
    {
        /* So part of the way DataQueryStorage works is that when it saves data to its local storage, it adds a marker
         *  to the saved NBT so that we don't accidentally re-save the same NBT over and over again with each use. */
        if (nbt == null || nbt.contains("wntToolbox.cached"))
            return;

        Toolbox.getEventBus().post(new CopyEntityToClipboard(new QueriedEntityDataSet(id, pos, nbt)));
    }

    @Inject(method = "copyBlock", at = @At(value = "HEAD"))
    public void injectCopyBlock(BlockState state, BlockPos pos, NbtCompound nbt, CallbackInfo ci)
    {
        /* So part of the way DataQueryStorage works is that when it saves data to its local storage, it adds a marker
         *  to the saved NBT so that we don't accidentally re-save the same NBT over and over again with each use. */
        if (nbt == null || nbt.contains("wntToolbox.cached"))
            return;

        Toolbox.getEventBus().post(new CopyBlockToClipboard(new QueriedBlockDataSet(state, pos, nbt)));
    }

    @Mixin(Keyboard.class)
    public interface KBAccessor
    {
        @Invoker
        void invokeCopyEntity(Identifier id, Vec3d pos, NbtCompound nbt);

        @Invoker
        void invokeCopyBlock(BlockState id, BlockPos pos, NbtCompound nbt);

        @Invoker
        void invokeDebugLog(Text text);
    }
}
