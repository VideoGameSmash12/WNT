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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import joptsimple.internal.Strings;
import lombok.Getter;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;
import org.apache.commons.lang3.ArrayUtils;

public abstract class FCommand implements Command<CommandSender>
{
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final String usage;

    public FCommand(String name, String description, String usage)
    {
        this.name = name;
        this.description = description;
        this.usage = !Strings.isNullOrEmpty(usage) ? usage : "/" + name;
    }

    @Override
    public int run(CommandContext<CommandSender> context)
    {
        try
        {
            if (!run(context.getSource(), context, ArrayUtils.remove(context.getInput().split(" "), 0)))
            {
                if (!Strings.isNullOrEmpty(description))
                {
                    context.getSource().sendMessage(description);
                }

                context.getSource().sendMessage("Usage: " + usage);
            }
        }
        catch (IllegalStateException ex)
        {
            context.getSource().sendMessage("Error: " + ex.getMessage());
        }
        catch (Throwable ex)
        {
            Fantasia.getServerLogger().error("An error occurred whilst attempting to execute command " + name, ex);
            context.getSource().sendMessage("Command error: " + ex.getMessage());
        }

        return 1;
    }

    public abstract boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args);
}
