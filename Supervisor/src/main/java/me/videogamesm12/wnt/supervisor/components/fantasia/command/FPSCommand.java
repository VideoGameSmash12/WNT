package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class FPSCommand extends FCommand
{
    public FPSCommand()
    {
        super("fps", "Returns a very basic FPS string.", "fps");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        sender.sendMessage(Supervisor.getInstance().getFPSText());
        return true;
    }
}
