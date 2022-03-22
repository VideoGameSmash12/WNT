package me.videogamesm12.w95.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.text.TranslatableText;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class WCommand implements Command<FabricClientCommandSource>, SuggestionProvider<FabricClientCommandSource>
{
    private final String name;
    private final String description;
    private final String usage;

    public WCommand(String name, String description, String usage)
    {
        this.name           = name;
        this.description    = description;
        this.usage          = usage;
    }

    public abstract boolean run(CommandContext<FabricClientCommandSource> context, String[] args);

    public abstract List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args);

    @Override
    public final int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException
    {
        if (!run(context, ArrayUtils.remove(context.getInput().split(" "), 0)))
            context.getSource().sendError(new TranslatableText("w95.messages.commands.usage", getUsage()));

        return 1;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException
    {
        return CommandSource.suggestMatching(suggest(context, ArrayUtils.remove(context.getInput().split(" "), 0)), builder);
    }

    public final WCommand register()
    {
        ClientCommandManager.DISPATCHER.register(
                ClientCommandManager.literal(name).then(ClientCommandManager.argument("args",
                        StringArgumentType.greedyString()).suggests(this).executes(this)).executes(this));

        return this;
    }

    public final String getName()
    {
        return name;
    }

    public final String getDescription()
    {
        return description;
    }

    public final String getUsage()
    {
        return usage;
    }
}
