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

package me.videogamesm12.wnt.supervisor.components.fantasia.session;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.Getter;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * <h1>CommandSender</h1>
 * <p>An implementation of CommandSource that allows commands to be run in the Fantasia console.</p>
 * @param session   An implementation of ISession
 */
public record CommandSender(@Getter ISession session) implements CommandSource
{
    /**
     * Sends a message to the CommandSender.
     * @param message   String
     */
    public void sendMessage(String message)
    {
        session.sendMessage(message);
    }

    @Override
    public Collection<String> getPlayerNames()
    {
        return Collections.emptyList();
    }

    @Override
    public Collection<String> getTeamNames()
    {
        return Collections.emptyList();
    }

    @Override
    public Stream<Identifier> getSoundIds()
    {
        return Stream.empty();
    }

    @Override
    public Stream<Identifier> getRecipeIds()
    {
        return Stream.empty();
    }

    @Override
    public CompletableFuture<Suggestions> getCompletions(CommandContext<?> context)
    {
        return Suggestions.empty();
    }

    @Override
    public Set<RegistryKey<World>> getWorldKeys()
    {
        return new HashSet<>();
    }

    @Override
    public DynamicRegistryManager getRegistryManager()
    {
        return null;
    }

    @Override
    public FeatureSet getEnabledFeatures()
    {
        return null;
    }

    @Override
    public CompletableFuture<Suggestions> listIdSuggestions(RegistryKey<? extends Registry<?>> registryRef, SuggestedIdType suggestedIdType, SuggestionsBuilder builder, CommandContext<?> context)
    {
        return null;
    }

    @Override
    public boolean hasPermissionLevel(int level)
    {
        return false;
    }

    public static LiteralArgumentBuilder<CommandSender> literal(String name)
    {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<CommandSender, T> argument(String name, ArgumentType<T> type)
    {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
