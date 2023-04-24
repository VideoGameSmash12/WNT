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

public class CommandSender implements CommandSource
{
    @Getter
    private final Session session;

    public CommandSender(Session session)
    {
        this.session = session;
    }

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
