package me.videogamesm12.w95.dumper;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.w95.Notifiable;
import me.videogamesm12.w95.W95;
import me.videogamesm12.w95.dumper.event.EntityDumpRequest;
import me.videogamesm12.w95.meta.ModuleInfo;
import me.videogamesm12.w95.module.WModule;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.io.File;

public class Dumper implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        W95.MODULES.register(DumperModule.class);
    }

    @ModuleInfo(name = "Dumper", description = "Allows you to dump entity data to disk.")
    public static class DumperModule extends Thread implements WModule, EntityDumpRequest
    {
        private DumperConfiguration CONFIG = null;

        @Override
        public void run()
        {
            AutoConfig.register(DumperConfiguration.class, GsonConfigSerializer::new);
            CONFIG = AutoConfig.getConfigHolder(DumperConfiguration.class).getConfig();
        }

        public synchronized void dumpEntityData(Notifiable source, int id)
        {
            if (!getConfiguration().isEnabled())
            {
                return;
            }

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

        @Override
        public DumperConfiguration getConfiguration()
        {
            return CONFIG;
        }

        @Override
        public ActionResult onEntityDumpRequested(Notifiable source, int id)
        {
            try
            {
                synchronized (this)
                {
                    dumpEntityData(source, id);
                }

                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                return ActionResult.FAIL;
            }
        }
    }

    public static class DumperConfiguration extends WModule.ModuleConfiguration
    {
        public DumperConfiguration()
        {
            super();
        }
    }
}