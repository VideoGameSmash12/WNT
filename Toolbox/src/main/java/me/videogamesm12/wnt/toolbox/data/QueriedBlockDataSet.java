package me.videogamesm12.wnt.toolbox.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class QueriedBlockDataSet
{
    private final BlockState state;
    private final BlockPos pos;
    private final @Nullable NbtCompound nbt;
    private long time = new Date().getTime();
}
