package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class ExitCommand extends FCommand
{
    public ExitCommand()
    {
        super("exit", "Disconnects you from the Telnet server.", "/exit");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        context.getSource().getSession().disconnect(false);
        return true;
    }
}
