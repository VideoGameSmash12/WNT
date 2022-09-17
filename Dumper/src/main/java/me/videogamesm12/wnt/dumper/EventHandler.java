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

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.dumper.events.RequestEntityDumpEvent;
import me.videogamesm12.wnt.dumper.events.RequestMapDumpEvent;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EventHandler extends Thread
{
    @Getter
    private EventBus eventBus = new EventBus();

    public EventHandler()
    {
        super("EventHandler");
        start();
    }

    @Override
    public void run()
    {
        eventBus.register(this);
    }

    @Subscribe
    public synchronized void onEntityDumpRequest(RequestEntityDumpEvent<FabricClientCommandSource> event)
    {
        World world = MinecraftClient.getInstance().world;
        FabricClientCommandSource source = event.getSource();

        if (world == null)
        {
            source.sendError(new TranslatableText("wnt.dumper.error.not_in_world"));
            return;
        }

        switch (event.getRequestType())
        {
            case MASS -> ((ClientWorldMixin) world).getEntityLookup().iterate().forEach(this::tryDump);
            case MULTIPLE, SINGULAR -> event.getEntities().forEach(this::tryDump);
        }

        source.sendFeedback(new TranslatableText("wnt.dumper.success"));
    }

    @Subscribe
    public synchronized void onMapDumpRequest(RequestMapDumpEvent<FabricClientCommandSource> event)
    {
        World world = MinecraftClient.getInstance().world;
        FabricClientCommandSource source = event.getSource();

        if (world == null)
        {
            source.sendError(new TranslatableText("wnt.dumper.error.not_in_world"));
            return;
        }

        try
        {
            switch (event.getRequestType())
            {
                case MASS -> ((ClientWorldMixin) world).getMapStates().forEach((key, value) -> tryDump(value, Integer.parseInt(key.replace("map_", ""))));
                case MULTIPLE, SINGULAR -> event.getMaps().forEach(id -> tryDump(((ClientWorldMixin) world).getMapStates().get("map_" + id), id));
            }
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to dump entities", ex);
            source.sendError(new TranslatableText("wnt.dumper.failed", new LiteralText(ex.getClass().getName())));
            return;
        }

        source.sendFeedback(new TranslatableText("wnt.dumper.success"));
    }

    /**
     * Safely writes an Entity's data to disk.
     * @param entity    Entity
     */
    public synchronized void tryDump(Entity entity)
    {
        try
        {
            dumpEntity(entity);
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Unable to dump entity " + entity.getUuidAsString(), ex);
        }
    }

    /**
     * Safely writes a MapState to disk.
     * @param map   MapState map
     * @param id    int
     */
    public synchronized void tryDump(MapState map, int id)
    {
        try
        {
            dumpMap(map, id);
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Unable to dump map " + id, ex);
        }
    }

    /**
     * Writes an Entity's data to disk.
     * @param entity        Entity
     * @throws IOException  If the attempt to write to disk fails
     */
    private synchronized void dumpEntity(@NotNull Entity entity) throws IOException
    {
        NbtCompound root = entity.writeNbt(new NbtCompound());
        NbtIo.write(root, new File(getDumpsFolder(),
                String.format("wnt.entity_%s_%s.nbt",
                        entity.getUuidAsString().toLowerCase(),
                        new Date().getTime())));
    }

    /**
     * Dumps a MapState to disk using the given ID.
     * @param map           MapState
     * @param id            int
     * @throws IOException  If the attempt to write to disk fails
     */
    private synchronized void dumpMap(@NotNull MapState map, int id) throws IOException
    {
        NbtCompound root = map.writeNbt(new NbtCompound());
        NbtIo.write(root, new File(getDumpsFolder(),
                String.format("wnt.map_%s_%s.dat",
                        id,
                        new Date().getTime())));
    }

    //--

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
