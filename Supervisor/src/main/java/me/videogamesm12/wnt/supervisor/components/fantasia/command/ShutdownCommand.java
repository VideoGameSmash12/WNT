package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class ShutdownCommand extends FCommand
{
    public ShutdownCommand()
    {
        super("shutdown", "Shuts the client down safely, forcefully, or (as a last resort) in a nuclear fashion.", "/shutdown [force | nuclear | safe]");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "nuclear" -> Runtime.getRuntime().halt(1337);
            case "force", "forcefully" ->
            {
                sender.sendMessage("Shutting down forcefully...");
                Supervisor.getInstance().shutdownForcefully();
            }
            case "safe", "safely" ->
            {
                sender.sendMessage("Shutting down safely...");
                Supervisor.getInstance().shutdownSafely();
            }
            default ->
            {
                return false;
            }
        }

        return true;
    }
}
