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
            case "entities" -> {
                MassEntityDumpRequest.EVENT.invoker().onEntityDumpRequested(context.getSource());
            }
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
