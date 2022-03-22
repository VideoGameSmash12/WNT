package me.videogamesm12.w95.toolbox.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.w95.command.WCommand;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.List;

public class ItemCryptCommand extends WCommand
{
    public ItemCryptCommand()
    {
        super("itemcrypt", "Encrypts items in your inventory for safe keeping.", "/itemcrypt <encrypt | decrypt | meta>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return false;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return null;
    }
}
