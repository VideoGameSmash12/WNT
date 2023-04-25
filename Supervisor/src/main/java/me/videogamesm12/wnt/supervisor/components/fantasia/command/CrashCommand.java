package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class CrashCommand extends FCommand
{
    public CrashCommand()
    {
        super("crash", "Intentionally crashes the client.", "/crash");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        sender.sendMessage("Setting flag to crash the client...");
        Supervisor.getInstance().getFlags().setSupposedToCrash(true);
        return true;
    }
}
