package me.videogamesm12.wnt.dumper;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.CommandSystem;
import me.videogamesm12.wnt.dumper.commands.DumpCommand;
import me.videogamesm12.wnt.dumper.event.EntityDumpRequest;
import me.videogamesm12.wnt.dumper.event.MapDumpRequest;
import me.videogamesm12.wnt.dumper.event.MassEntityDumpRequest;
import me.videogamesm12.wnt.dumper.event.MassMapDumpRequest;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import me.videogamesm12.wnt.meta.ModuleInfo;
import me.videogamesm12.wnt.module.WModule;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
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
        WNT.MODULES.register(DumperModule.class);
        CommandSystem.registerCommand(DumpCommand.class);
    }

    @ModuleInfo(name = "Dumper", description = "Allows you to dump entity data to disk.")
    public static class DumperModule extends Thread implements WModule, EntityDumpRequest, MassEntityDumpRequest, MapDumpRequest, MassMapDumpRequest
    {
        public DumperModule()
        {
            EntityDumpRequest.EVENT.register(this);
            MassEntityDumpRequest.EVENT.register(this);
            MapDumpRequest.EVENT.register(this);
            MassMapDumpRequest.EVENT.register(this);
            //--
            start();
        }

        @Override
        public void run()
        {
        }

        public synchronized void dumpMapData(int id)
        {
            World world = MinecraftClient.getInstance().world;

            if (world == null)
            {
                throw new IllegalStateException("Not currently in a world");
            }

            MapState map = world.getMapState("map_" + id);

            if (map == null)
            {
                throw new IllegalArgumentException("That map isn't loaded in memory");
            }

            NbtCompound root = map.writeNbt(new NbtCompound());

            try
            {
                NbtIo.write(root, new File(getDumpsFolder(), "wnt.map_" + id + "_" + new Date().getTime() + ".nbt"));
            }
            catch (Exception ex)
            {
                WNT.LOGGER.error(ex);
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
                NbtIo.write(root, new File(getDumpsFolder(), "wnt.entity_" + entity.getUuidAsString().toLowerCase() + "_" + new Date().getTime() + ".nbt"));
            }
            catch (Exception ex)
            {
                WNT.LOGGER.error(ex);
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

            // This is a stupid way of doing this, but it's fine for now
            ((ClientWorldMixin) world).getMapStates().keySet().forEach((id) -> dumpMapData(Integer.parseInt(id.replace("map_", ""))));
        }

        @Override
        public ActionResult onEntityDumpRequested(FabricClientCommandSource source, int id)
        {
            try
            {
                CompletableFuture.runAsync(() -> {
                    dumpEntityData(id);
                    source.sendFeedback(new TranslatableText("wnt.dumper.success"));
                });
                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                source.sendError(Text.of(ex.getMessage()));
                ex.printStackTrace();
                return ActionResult.FAIL;
            }
        }

        @Override
        public ActionResult onEntityDumpRequested(FabricClientCommandSource source)
        {
            try
            {
                CompletableFuture.runAsync(() -> {
                    dumpAllEntityData();
                    source.sendFeedback(new TranslatableText("wnt.dumper.success"));
                });
                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                source.sendError(Text.of(ex.getMessage()));
                ex.printStackTrace();
                return ActionResult.FAIL;
            }
        }

        @Override
        public ActionResult onMapDumpRequested(FabricClientCommandSource source)
        {
            try
            {
                CompletableFuture.runAsync(() -> {
                    dumpAllMapData();
                    source.sendFeedback(new TranslatableText("wnt.dumper.success"));
                });
                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                source.sendError(Text.of(ex.getMessage()));
                ex.printStackTrace();
                return ActionResult.FAIL;
            }
        }

        @Override
        public ActionResult onMapDumpRequested(int id, FabricClientCommandSource source)
        {
            try
            {
                CompletableFuture.runAsync(() -> {
                    dumpMapData(id);
                    source.sendFeedback(new TranslatableText("wnt.dumper.success"));
                });
                return ActionResult.PASS;
            }
            catch (Exception ex)
            {
                source.sendError(Text.of(ex.getMessage()));
                ex.printStackTrace();
                return ActionResult.FAIL;
            }
        }
    }

    public static File getDumpsFolder()
    {
        File folder = new File(WNT.getWNTFolder(), "dumps");

        if (!folder.isDirectory())
        {
            folder.mkdirs();
        }

        return folder;
    }
}