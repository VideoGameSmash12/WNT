package me.videogamesm12.wnt.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.command.WCommand;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.kyori.adventure.text.Component;

import java.util.List;

public class WNTCommand extends WCommand
{
    public WNTCommand()
    {
        super("wnt", "Manages all things related to WNT.", "/wnt <modules>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
            return false;

        switch (args[0].toLowerCase())
        {
            case "modules" -> {

                msg(Component.translatable(""));
            }
        }

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return null;
    }
}
