package me.videogamesm12.w95.toolbox;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.videogamesm12.w95.command.CommandSystem;
import me.videogamesm12.w95.toolbox.commands.NameCommand;
import me.videogamesm12.w95.toolbox.commands.UuidCommand;
import net.fabricmc.api.ModInitializer;

public class Toolbox implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CommandSystem.registerCommand(NameCommand.class);
        CommandSystem.registerCommand(UuidCommand.class);
    }
}