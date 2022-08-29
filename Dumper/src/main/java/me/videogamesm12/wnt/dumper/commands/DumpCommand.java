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

package me.videogamesm12.wnt.dumper.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.dumper.Dumper;
import me.videogamesm12.wnt.dumper.events.RequestEntityDumpEvent;
import me.videogamesm12.wnt.dumper.events.RequestMapDumpEvent;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DumpCommand extends WCommand
{
    public DumpCommand()
    {
        super("dump", "", "/dump <entities | entity <id> | map <id> | maps>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
            return false;

        if (MinecraftClient.getInstance().world == null)
        {
            context.getSource().sendError(new TranslatableText("wnt.dumper.error.not_in_world"));
            return true;
        }

        switch (args[0].toLowerCase())
        {
            case "entities" -> CompletableFuture.runAsync(() -> Dumper.getHandler().getEventBus().post(new RequestEntityDumpEvent<>(context.getSource())));
            case "entity" -> {
                if (args.length < 2)
                    return false;

                int id = Integer.parseInt(args[1]);

                CompletableFuture.runAsync(() -> {
                    Entity entity = MinecraftClient.getInstance().world.getEntityById(id);

                    if (entity == null)
                    {
                        context.getSource().sendError(new TranslatableText("wnt.dumper.error.entity_not_found"));
                        return;
                    }

                    Dumper.getHandler().getEventBus().post(new RequestEntityDumpEvent<>(entity, context.getSource()));
                });
            }
            case "maps" -> CompletableFuture.runAsync(() -> Dumper.getHandler().getEventBus().post(new RequestMapDumpEvent<>(context.getSource())));
            case "map" -> {
                if (args.length < 2)
                    return false;

                int id = Integer.parseInt(args[1]);

                CompletableFuture.runAsync(() -> {
                    if (!((ClientWorldMixin) MinecraftClient.getInstance().world).getMapStates().containsKey("map_" + id))
                    {
                        context.getSource().sendError(new TranslatableText("wnt.dumper.map_not_loaded"));
                        return;
                    }

                    Dumper.getHandler().getEventBus().post(new RequestMapDumpEvent<>(id, context.getSource()));
                });
            }
            default -> {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return new ArrayList<>();
    }
}
