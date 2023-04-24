package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.FantasiaSupervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class DisconnectCommand extends FCommand
{
    public DisconnectCommand()
    {
        super("disconnect", "Disconnects you from the server you are currently connected to. Requires you to be connected to a server for this to work.", "/disconnect");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        sender.sendMessage("Disconnecting from the server...");
        FantasiaSupervisor.getInstance().disconnect();
        return true;
    }
}
