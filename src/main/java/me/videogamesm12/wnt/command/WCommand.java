/*
 * Copyright (c) 2022 Video
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

package me.videogamesm12.wnt.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.module.ModuleNotEnabledException;
import me.videogamesm12.wnt.util.Messenger;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.command.CommandSource;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
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

    public abstract boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
            throws ModuleNotEnabledException;

    public abstract List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args);

    @Override
    public final int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException
    {
        try
        {
            if (!run(context, ArrayUtils.remove(context.getInput().split(" "), 0)))
                msg(Component.translatable("wnt.messages.commands.usage", Component.text(getUsage()))
                        .color(NamedTextColor.RED));
        }
        catch (NumberFormatException ex)
        {
            msg(Component.translatable("wnt.messages.general.invalid_number", NamedTextColor.RED));
        }
        catch (ModuleNotEnabledException ex)
        {
            msg(Component.translatable("wnt.messages.general.module_not_enabled",
                    Component.text(ex.getModule().getMeta().name())).color(NamedTextColor.RED));
        }
        catch (Throwable ex)
        {
            msg(Component.translatable("wnt.messages.general.command_error", Component.text(ex.getMessage()))
                    .color(NamedTextColor.RED));
            WNT.getLogger().error("Command " + name + " threw an exception whilst attempting to execute", ex);
        }

        return 1;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException
    {
        List<String> suggestions = suggest(context, ArrayUtils.remove(context.getInput().split(" "), 0));
        return CommandSource.suggestMatching(suggestions != null ? suggestions : new ArrayList<>(), builder);
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

    protected void msg(Component component)
    {
        Messenger.sendChatMessage(component);
    }
}
