package me.videogamesm12.w95.dumper;

import me.videogamesm12.w95.Notifiable;
import me.videogamesm12.w95.W95;
import me.videogamesm12.w95.module.WModule;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.io.File;

public class Dumper implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        W95.MODULES.register(DumperModule.class);
    }

    public static class DumperModule extends Thread implements WModule
    {
        @Override
        public void run()
        {
        }

        public synchronized void dumpEntityData(Notifiable source, int id)
        {
            World world = MinecraftClient.getInstance().world;

            if (world == null)
            {
                throw new IllegalStateException("Not currently in a world");
            }

            Entity entity = world.getEntityById(id);

            if (entity == null)
            {
                throw new IllegalArgumentException("Entity not found");
            }

            //==-- Dumps the data to disk --==//
            NbtCompound root = entity.writeNbt(new NbtCompound());

            try
            {
                NbtIo.write(root, new File(W95.getW95Folder(), "dumps/" + entity.hashCode() + ".nbt"));
            }
            catch (Exception ex)
            {
                W95.LOGGER.error(ex);
                throw new IllegalStateException("Dump failed");
            }

            source.sendNotification(Text.of("Entity dumped. Location is /w95/dumps/" + entity.hashCode() + ".nbt"), Notifiable.NotificationType.INFORMATION);
        }
    }
}