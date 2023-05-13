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
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;
import me.videogamesm12.wnt.supervisor.enums.InventoryType;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.visitor.StringNbtWriter;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListCommand extends FCommand
{
    public ListCommand()
    {
        super("list", "Returns a list of every player on the server, entities nearby, loaded maps, or items in your inventory.", "list <entities | inventory [-nbt] | maps [-expanded] | players [-expanded]> ");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "entities" ->
            {
                sender.sendMessage("-- == ++ ENTITY DUMP ++ == --");
                entitiesToStringList(Supervisor.getInstance().getEntities()).forEach(sender::sendMessage);
            }
            case "inventory" ->
            {
                final boolean showNbt = args.length == 2 && args[1].equalsIgnoreCase("-nbt");
                sender.sendMessage("-- == ++ INVENTORY DUMP ++ == --");
                inventoryToStringList(Supervisor.getInstance().getInventory(), showNbt).forEach(sender::sendMessage);
            }
            case "maps" ->
            {
                final boolean expanded = args.length == 2 && args[1].equalsIgnoreCase("-expanded");
                sender.sendMessage("-- == ++ MAP DUMP ++ == --");
                mapsToStringList(Supervisor.getInstance().getMaps(), expanded).forEach(sender::sendMessage);
            }
            case "players" ->
            {
                final boolean expanded = args.length == 2 && args[1].equalsIgnoreCase("-expanded");
                sender.sendMessage("List of online players:");
                playersToStringList(Supervisor.getInstance().getPlayerList(), expanded).forEach(sender::sendMessage);
            }
            default ->
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts an entity list provided by the Supervisor to a list of messages that can be sent through Fantasia.
     * @param entities  List of entities
     * @return          A list of all items in your inventory as a String
     */
    private List<String> entitiesToStringList(final Iterable<Entity> entities)
    {
        final List<String> list = new ArrayList<>();

        entities.forEach(entity -> list.add(String.format("- Name: %s | Type: %s | Location: %s | ID: %s | UUID: %s",
                entity.getDisplayName() != null ? entity.getDisplayName().getString() : entity.getEntityName(),
                EntityType.getId(entity.getType()).toString(),
                String.format("%s, %s, %s", entity.getX(), entity.getY(), entity.getZ()),
                entity.getId(),
                entity.getUuidAsString()
        )));

        return list;
    }

    /**
     * Converts an inventory provided by the Supervisor to a list of messages that can be sent through Fantasia.
     * @param items     Map of items with their inventory type
     * @param showNbt   Whether to show NBT in these messages
     * @return          A list of all items in your inventory as a String
     */
    private List<String> inventoryToStringList(final Map<InventoryType, List<ItemStack>> items, final boolean showNbt)
    {
        final List<String> list = new ArrayList<>();
        items.forEach((entry, itemList) -> {
            itemList.stream().filter((item) ->
            {
                final RegistryEntry<Item> key = item.getRegistryEntry();
                return key.getKey().isPresent() && !key.getKey().get().getValue().equals(new Identifier("minecraft", "air"));
            }).forEach(item ->
            {
                String result = showNbt ? "- Name: %s | ID: %s | Count: %s | Location: %s | NBT: %s" : "- Name: %s | ID: %s | Count: %s | Location: %s";

                result = String.format(result,
                        item.getName().getString(),
                        item.getRegistryEntry().getKey().get().getValue().toString(),
                        item.getCount(),
                        StringUtils.capitalize(entry.name().toLowerCase()),
                        showNbt ? item.getNbt() != null ? new StringNbtWriter().apply(item.getNbt()) : "{}" : "");

                list.add(result);
            });
        });
        return list;
    }

    /**
     * Converts a map of MapStates provided by the Supervisor to a list of messages that can be sent through Fantasia.
     * @param maps      Map of MapStates
     * @param expanded  Should we show an expanded version of the list?
     * @return          A list of all maps in memory as a String
     */
    private List<String> mapsToStringList(final Map<String, MapState> maps, final boolean expanded)
    {
        final List<String> list = new ArrayList<>();
        maps.forEach((id, state) ->
        {
            String result = expanded ? "- ID: %s | Scale: %s | World: %s | Center X: %s | Center Z: %s | Locked: %s"
                    : "- ID: %s | Scale: %s | World: %s";

            result = String.format(result,
                    id,
                    state.scale,
                    state.dimension.getValue().toString(),
                    expanded ? state.centerX : "",
                    expanded ? state.centerZ : "",
                    expanded ? state.locked : "");

            list.add(result);
        });

        return list;
    }

    /**
     * Converts a player list provided by the Supervisor to a list of messages that can be sent through Fantasia.
     * @param players   List of players
     * @param expanded  Should we show an expanded version of the list?
     * @return          A list of all players online as a String
     */
    private List<String> playersToStringList(final List<PlayerListEntry> players, final boolean expanded)
    {
        final List<String> list = new ArrayList<>();
        players.forEach(entry ->
        {
            String result = expanded ? "- Username: %s | Display Name: %s | UUID: %s | Ping: %sms | Gamemode: %s | Skin Model: %s | Skin ID: %s"
                    : "- Username: %s | Display Name: %s | UUID: %s | Ping: %sms";

            result = String.format(result,
                    entry.getProfile().getName(),
                    entry.getDisplayName() != null ? entry.getDisplayName().getString() : "(none)",
                    entry.getProfile().getId().toString(),
                    entry.getLatency(),
                    expanded ? entry.getGameMode().getName() : "",
                    expanded ? entry.getModel() : "",
                    expanded ? entry.getSkinTexture().toString() : "");

            list.add(result);
        });

        return list;
    }
}
