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

import com.google.common.eventbus.Subscribe;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.dumper.events.DumpResultEvent;
import me.videogamesm12.wnt.dumper.events.RequestEntityDumpEvent;
import me.videogamesm12.wnt.dumper.events.RequestMapDumpEvent;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import me.videogamesm12.wnt.util.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class EventHandler
{
    public static final Identifier INGAME_IDENTIFIER = Identifier.of("minecraft", "ingame");

    private final ThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(8);

    public EventHandler()
    {
        WNT.getEventBus().register(this);
    }

    @Subscribe
    public void onDumpResult(DumpResultEvent event)
    {
        if (event.getRequester().equals(INGAME_IDENTIFIER))
        {
            Messenger.sendChatMessage(event.getMessage());
        }
    }

    @Subscribe
    public void onEntityDumpRequest(RequestEntityDumpEvent event)
    {
        pool.execute(() ->
        {
            World world = MinecraftClient.getInstance().world;

            if (world == null)
            {
                WNT.getEventBus().post(new DumpResultEvent(event.getRequester(), ActionResult.FAIL,
                        Component.translatable("wnt.dumper.error.not_in_world", NamedTextColor.RED)));
                return;
            }

            switch (event.getRequestType())
            {
                case MASS -> ((ClientWorldMixin) world).getEntityLookup().iterate().forEach(this::tryDump);
                case MULTIPLE, SINGULAR -> event.getEntities().forEach(this::tryDump);
            }

            WNT.getEventBus().post(new DumpResultEvent(event.getRequester(), ActionResult.SUCCESS,
                    Component.translatable("wnt.dumper.success", NamedTextColor.GREEN)));
        });
    }

    @Subscribe
    public void onMapDumpRequest(RequestMapDumpEvent event)
    {
        pool.execute(() ->
        {
            World world = MinecraftClient.getInstance().world;

            if (world == null)
            {
                WNT.getEventBus().post(new DumpResultEvent(event.getRequester(), ActionResult.FAIL,
                        Component.translatable("wnt.dumper.error.not_in_world", NamedTextColor.RED)));
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
                WNT.getEventBus().post(new DumpResultEvent(event.getRequester(), ActionResult.FAIL,
                        Component.translatable("wnt.dumper.failed", Component.text(ex.getMessage())).color(NamedTextColor.RED)));
                WNT.getLogger().error("Failed to dump entities", ex);
                return;
            }

            WNT.getEventBus().post(new DumpResultEvent(event.getRequester(), ActionResult.SUCCESS,
                    Component.translatable("wnt.dumper.success", NamedTextColor.GREEN)));
        });
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
    private void dumpEntity(@NotNull Entity entity) throws IOException
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
    private void dumpMap(@NotNull MapState map, int id) throws IOException
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
