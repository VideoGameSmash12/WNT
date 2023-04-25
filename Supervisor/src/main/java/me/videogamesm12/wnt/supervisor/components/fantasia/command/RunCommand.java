package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class RunCommand extends FCommand
{
    public RunCommand()
    {
        super("run", "Executes an in-game command. Requires you to be connected to a server for this to work.", "/run <command>");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        String command = String.join(" ", args);
        sender.sendMessage("Sending command: /" + command);
        Supervisor.getInstance().runCommand(command);
        return true;
    }
}
