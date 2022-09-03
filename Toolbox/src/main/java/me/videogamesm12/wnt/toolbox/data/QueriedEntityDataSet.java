package me.videogamesm12.wnt.toolbox.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class QueriedEntityDataSet
{
    private final Identifier entityIdentifier;
    private final Vec3d pos;
    private final NbtCompound nbt;
    private long time = new Date().getTime();
}
