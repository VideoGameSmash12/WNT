package me.videogamesm12.w95.dumper;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.w95.Notifiable;
import me.videogamesm12.w95.W95;
import me.videogamesm12.w95.command.CommandSystem;
import me.videogamesm12.w95.dumper.commands.DumpCommand;
import me.videogamesm12.w95.dumper.event.EntityDumpRequest;
import me.videogamesm12.w95.dumper.event.MassEntityDumpRequest;
import me.videogamesm12.w95.dumper.event.MassMapDumpRequest;
import me.videogamesm12.w95.dumper.mixin.ClientWorldMixin;
import me.videogamesm12.w95.meta.ModuleInfo;
import me.videogamesm12.w95.module.WModule;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.io.File;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class Dumper implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        W95.MODULES.register(DumperModule.class);
        CommandSystem.registerCommand(DumpCommand.class);
    }

    @ModuleInfo(name = "Dumper", description = "Allows you to dump entity data to disk.")
    public static class DumperModule extends Thread implements WModule, EntityDumpRequest, MassEntityDumpRequest, MassMapDumpRequest
    {
        private DumperConfiguration CONFIG = null;

        public DumperModule()
        {
            EntityDumpRequest.EVENT.register(this);
            MassEntityDumpRequest.EVENT.register(this);
            MassMapDumpRequest.EVENT.register(this);
            //--
            start();
        }

        @Override
        public void run()
        {
            AutoConfig.register(DumperConfiguration.class, GsonConfigSerializer::new);
            CONFIG = AutoConfig.getConfigHolder(DumperConfiguration.class).getConfig();
        }

        public synchronized void dumpMapData(MapState map)
        {
            NbtCompound root = map.writeNbt(new NbtCompound());

            try
            {
                NbtIo.write(root, new File(getDumpsFolder(), "w95.map_" + map.hashCode() + "_" + new Date().getTime() + ".nbt"));
            }
            catch (Exception ex)
            {
                W95.LOGGER.error(ex);
                throw new IllegalStateException("Dump failed");
            }
        }

        public synchronized void dumpEntityData(int id)
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
                NbtIo.write(root, new File(getDumpsFolder(), "w95.entity_" + entity.getUuidAsString().toLowerCase() + "_" + new Date().getTime() + ".nbt"));
            }
            catch (Exception ex)
            {
                W95.LOGGER.error(ex);
                throw new IllegalStateException("Dump failed");
            }
        }

        public synchronized void dumpAllEntityData()
        {
            World world = MinecraftClient.getInstance().world;

            if (world == null)
            {
                throw new IllegalStateException("Not currently in a world");
            }

            ((ClientWorldMixin) world).getEntityLookup().iterate().forEach((entity) -> {
                dumpEntityData(entity.getId());
            });
        }

        public synchronized void dumpAllMapData()
        {
            World world = MinecraftClient.getInstance().world;

            if (world == null)
            {
                throw new IllegalStateException("Not currently in a world");
            }

            ((ClientWorldMixin) world).getMapStates().values().forEach(this::dumpMapData);
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
                CompletableFuture.runAsync(() -> {
                    dumpEntityData(id);
                    source.sendNotification(new TranslatableText("w95.dumper.success"), Notifiable.NotificationType.INFORMATION);
                });
                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                source.sendNotification(Text.of(ex.getMessage()), Notifiable.NotificationType.ERROR);
                ex.printStackTrace();
                return ActionResult.FAIL;
            }
        }

        @Override
        public ActionResult onEntityDumpRequested(Notifiable source)
        {
            try
            {
                CompletableFuture.runAsync(() -> {
                    dumpAllEntityData();
                    source.sendNotification(new TranslatableText("w95.dumper.success"), Notifiable.NotificationType.INFORMATION);
                });
                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                source.sendNotification(Text.of(ex.getMessage()), Notifiable.NotificationType.ERROR);
                ex.printStackTrace();
                return ActionResult.FAIL;
            }
        }

        @Override
        public ActionResult onMapDumpRequested(Notifiable source)
        {
            try
            {
                CompletableFuture.runAsync(() -> {
                    dumpAllMapData();
                    source.sendNotification(new TranslatableText("w95.dumper.success"), Notifiable.NotificationType.INFORMATION);
                });
                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                source.sendNotification(Text.of(ex.getMessage()), Notifiable.NotificationType.ERROR);
                ex.printStackTrace();
                return ActionResult.FAIL;
            }
        }
    }

    @Config(name = "w95-dumper")
    public static class DumperConfiguration extends WModule.ModuleConfiguration
    {
        public DumperConfiguration()
        {
            super();
        }
    }

    public static File getDumpsFolder()
    {
        File folder = new File(W95.getW95Folder(), "dumps");

        if (!folder.isDirectory())
        {
            folder.mkdirs();
        }

        return folder;
    }
}