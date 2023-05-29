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

package me.videogamesm12.wnt.supervisor.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.module.ModuleNotEnabledException;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.InGameSession;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class FantasiaCommand extends WCommand
{
    private final InGameSession inGameSession;

    public FantasiaCommand()
    {
        super("fantasia", "Interface with Fantasia. Requires 'runCommandsFromInGameAllowed' to be set to true in the Supervisor configuration.", "/fantasia <command>");

        if (Supervisor.getConfig().getFantasiaSettings().isRunningCommandsFromInGameAllowed())
        {
            inGameSession = new InGameSession();
        }
        else
        {
            inGameSession = null;
        }
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args) throws ModuleNotEnabledException
    {
        if (!Supervisor.getConfig().getFantasiaSettings().isRunningCommandsFromInGameAllowed())
        {
            msg(Component.translatable("wnt.supervisor.command.fantasia.not_allowed", NamedTextColor.RED));
            return true;
        }
        else if (args.length == 0)
        {
            return false;
        }

        try
        {
            final String command = String.join(" ", args);
            msg(Component.translatable("wnt.supervisor.command.fantasia.running", Component.text(command)).color(NamedTextColor.GREEN));
            Fantasia.getInstance().getServer().getDispatcher().execute(command, inGameSession.getSender());
        }
        catch (CommandSyntaxException ex)
        {
            msg(Component.translatable("wnt.supervisor.command.fantasia.unknown_command", Component.text(args[0])).color(NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return Fantasia.getInstance().getServer().getCommands().keySet().stream().toList();
    }
}
