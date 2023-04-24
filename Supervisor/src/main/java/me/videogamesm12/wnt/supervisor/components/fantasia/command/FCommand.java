package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import joptsimple.internal.Strings;
import lombok.Getter;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.CompletableFuture;

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
