package me.videogamesm12.w95.toolbox;

import me.videogamesm12.w95.command.CommandSystem;
import me.videogamesm12.w95.toolbox.commands.UuidCommand;
import net.fabricmc.api.ModInitializer;

public class Toolbox implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CommandSystem.registerCommand(UuidCommand.class);
    }
}