package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.FantasiaSupervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class ShutdownCommand extends FCommand
{
    public ShutdownCommand()
    {
        super("shutdown", "Shuts the client down either forcefully or safely.", "/shutdown [force | safe]");
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
            case "force", "forcefully" ->
            {
                sender.sendMessage("Shutting down forcefully...");
                FantasiaSupervisor.getInstance().shutdownForcefully();
            }
            case "safe", "safely" ->
            {
                sender.sendMessage("Shutting down safely...");
                FantasiaSupervisor.getInstance().shutdownSafely();
            }
            default ->
            {
                return false;
            }
        }

        return true;
    }
}
