package me.videogamesm12.w95.dumper.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.w95.Notifiable;
import me.videogamesm12.w95.command.WCommand;
import me.videogamesm12.w95.dumper.event.EntityDumpRequest;
import me.videogamesm12.w95.dumper.event.MassEntityDumpRequest;
import me.videogamesm12.w95.dumper.event.MassMapDumpRequest;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.ArrayList;
import java.util.List;

public class DumpCommand extends WCommand
{
    public DumpCommand()
    {
        super("dump", "", "/dump <entities | entity <id> | maps>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
            return false;

        switch (args[0].toLowerCase())
        {
            case "entities" -> {
                MassEntityDumpRequest.EVENT.invoker().onEntityDumpRequested(Notifiable.inGame());
            }
            case "entity" -> {
                if (args.length < 2)
                    return false;

                int id = Integer.parseInt(args[1]);

                EntityDumpRequest.EVENT.invoker().onEntityDumpRequested(Notifiable.inGame(), id);
            }
            case "maps" -> {
                MassMapDumpRequest.EVENT.invoker().onMapDumpRequested(Notifiable.inGame());
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
