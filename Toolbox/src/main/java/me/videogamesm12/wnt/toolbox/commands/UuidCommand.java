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

package me.videogamesm12.wnt.toolbox.commands;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.toolbox.util.AshconUtil;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class UuidCommand extends WCommand
{
    private final Pattern usernamePattern = Pattern.compile("^\\w{3,20}$");

    public UuidCommand()
    {
        super("uuid", "Returns the UUID of a given player", "/uuid <player>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (!usernamePattern.matcher(args[0]).matches())
        {
            msg(Component.translatable("wnt.toolbox.ashcon.error.bad_username",
                    Component.text(args[0])).color(NamedTextColor.RED));
            return true;
        }

        CompletableFuture.supplyAsync(() -> {
            try
            {
                return AshconUtil.getAshconData(args[0].toLowerCase());
            }
            catch (FileNotFoundException ex)
            {
                msg(Component.translatable("wnt.toolbox.ashcon.error.player_not_found", NamedTextColor.RED));
            }
            catch (JsonParseException ex)
            {
                msg(Component.translatable("wnt.toolbox.ashcon.error.bad_json", NamedTextColor.RED));
            }
            catch (Throwable ex)
            {
                msg(Component.translatable("wnt.toolbox.ashcon.error.unknown", NamedTextColor.RED).hoverEvent(
                        HoverEvent.showText(Component.text(ex.getMessage()))));
                WNT.getLogger().error("Details of the error: ", ex);
            }

            return null;
        }).whenComplete((result, ex) -> {
            if (result == null) return;

            msg(Component.translatable("wnt.messages.command.uuid.result",
                    Component.text(result.getUsername())
                            .color(NamedTextColor.WHITE),
                    Component.text(result.getUuid())
                            .color(NamedTextColor.WHITE)
                            .decorate(TextDecoration.UNDERLINED)
                            .clickEvent(ClickEvent.copyToClipboard(result.getUuid()))
                            .hoverEvent(HoverEvent.showText(Component.translatable("chat.copy.click"))))
                    .colorIfAbsent(NamedTextColor.GRAY));
        });

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return new ArrayList<>();
    }
}
