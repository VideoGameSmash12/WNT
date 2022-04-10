package me.videogamesm12.wnt.toolbox;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.CommandSystem;
import me.videogamesm12.wnt.toolbox.commands.NameCommand;
import me.videogamesm12.wnt.toolbox.commands.TelnetCommand;
import me.videogamesm12.wnt.toolbox.commands.UuidCommand;
//import me.videogamesm12.wnt.toolbox.modules.DoomHammer;
import me.videogamesm12.wnt.toolbox.modules.NuTelnet;
import net.fabricmc.api.ModInitializer;

public class Toolbox implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        WNT.MODULES.register(NuTelnet.class);
        //WNT.MODULES.register(DoomHammer.class);
        //--
        CommandSystem.registerCommand(NameCommand.class);
        CommandSystem.registerCommand(UuidCommand.class);
        CommandSystem.registerCommand(TelnetCommand.class);
    }
}