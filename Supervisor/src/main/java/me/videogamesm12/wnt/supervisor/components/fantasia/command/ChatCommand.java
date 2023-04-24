package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.FantasiaSupervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

public class ChatCommand extends FCommand
{
    public ChatCommand()
    {
        super("chat", "Sends a chat message to the server.", "/chat <message>");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        String message = String.join(" ", args);
        sender.sendMessage("Sending message: " + message);
        FantasiaSupervisor.getInstance().chatMessage(message);
        return true;
    }
}
