package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import joptsimple.internal.Strings;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

import java.util.Comparator;
import java.util.Map;

public class HelpCommand extends FCommand
{
    public HelpCommand()
    {
        super("help", "Lists all available commands and shows command help.", "/help [command]");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        Map<String, FCommand> commands = Fantasia.getInstance().getServer().getCommands();

        if (args.length == 0)
        {
            sender.sendMessage(" -- == ++ COMMAND HELP ++ == --");
            sender.sendMessage(" Use 'help <command>' to see a command's usage.");
            commands.values().stream().sorted(Comparator.comparing(FCommand::getName)).forEach(command -> sender.sendMessage(" - " + command.getName() + ": " + command.getDescription()));
            return true;
        }

        FCommand command = commands.getOrDefault(args[0].toLowerCase(), null);
        if (command == null)
        {
            sender.sendMessage("Command not found: " + args[0]);
        }
        else
        {
            if (!Strings.isNullOrEmpty(command.getDescription()))
            {
                context.getSource().sendMessage(command.getDescription());
            }

            context.getSource().sendMessage("Usage: " + command.getUsage());
        }
        return true;
    }
}
