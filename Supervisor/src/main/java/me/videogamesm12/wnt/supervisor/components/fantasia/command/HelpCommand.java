/*
 * Copyright (c) 2023 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
