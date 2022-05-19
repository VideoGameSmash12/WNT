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

package me.videogamesm12.wnt.dumper;

import me.shedaniel.autoconfig.annotation.Config;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.CommandSystem;
import me.videogamesm12.wnt.dumper.commands.DumpCommand;
import me.videogamesm12.wnt.dumper.event.EntityDumpRequest;
import me.videogamesm12.wnt.dumper.event.MapDumpRequest;
import me.videogamesm12.wnt.dumper.event.MassEntityDumpRequest;
import me.videogamesm12.wnt.dumper.event.MassMapDumpRequest;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import me.videogamesm12.wnt.module.Module;
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
    private static DumperThread thread = null;

    @Override
    public void onInitialize()
    {
        WNT.MODULES.register(DumperModule.class);
        CommandSystem.registerCommand(DumpCommand.class);
        //--
        thread = new DumperThread();
    }

    @Config(name = "wnt-dumper")
    public static class DumperClass extends Module.MConfig
    {
    }

    public static class DumperModule extends Module implements EntityDumpRequest, MassEntityDumpRequest, MapDumpRequest,
            MassMapDumpRequest
    {
        public DumperModule()
        {
            super();

            EntityDumpRequest.EVENT.register(this);
            MassEntityDumpRequest.EVENT.register(this);
            MapDumpRequest.EVENT.register(this);
            MassMapDumpRequest.EVENT.register(this);
        }

        @Override
        public void onStart()
        {
        }

        @Override
        public void onStop()
        {
        }

        @Override
        public Class<DumperClass> getConfigClass()
        {
            return DumperClass.class;
        }

        @Override
        public ActionResult onEntityDumpRequested(FabricClientCommandSource source, int id)
        {
            try
            {
                CompletableFuture.runAsync(() -> {
                    thread.dumpEntityData(id);
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
                    thread.dumpAllEntityData();
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
                    thread.dumpAllMapData();
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
                    thread.dumpMapData(id);
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

    public static class DumperThread extends Thread
    {
        public DumperThread()
        {
            super("Dumper");
            start();
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
                NbtIo.write(root, new File(getDumpsFolder(), "wnt.map_" + id + "_" + new Date().getTime() + ".dat"));
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