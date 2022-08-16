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
import me.videogamesm12.wnt.dumper.event.EntityDumpRequest;
import me.videogamesm12.wnt.dumper.event.MapDumpRequest;
import me.videogamesm12.wnt.dumper.event.MassEntityDumpRequest;
import me.videogamesm12.wnt.dumper.event.MassMapDumpRequest;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

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

        switch (args[0].toLowerCase())
        {
            case "entities" -> MassEntityDumpRequest.EVENT.invoker().onEntityDumpRequested(context.getSource());
            case "entity" -> {
                if (args.length < 2)
                    return false;

                int id = Integer.parseInt(args[1]);

                EntityDumpRequest.EVENT.invoker().onEntityDumpRequested(context.getSource(), id);
            }
            case "map" -> {
                if (args.length < 2)
                    return false;

                int id = Integer.parseInt(args[1]);

                if (!((ClientWorldMixin) MinecraftClient.getInstance().world).getMapStates().containsKey("map_" + id))
                {
                    context.getSource().sendError(new TranslatableText("wnt.dumper.map_not_loaded"));
                    return true;
                }

                MapDumpRequest.EVENT.invoker().onMapDumpRequested(id, context.getSource());
            }
            case "maps" -> {
                MassMapDumpRequest.EVENT.invoker().onMapDumpRequested(context.getSource());
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
