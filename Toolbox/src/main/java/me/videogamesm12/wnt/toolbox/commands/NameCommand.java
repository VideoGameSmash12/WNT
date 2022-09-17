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

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.toolbox.util.AshconUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class NameCommand extends WCommand
{
    public NameCommand()
    {
        super("name", "", "/name <uuid>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        CompletableFuture.supplyAsync(() -> {
            try
            {
                return AshconUtil.getAshconData(UUID.fromString(args[0].toLowerCase()).toString());
            }
            catch (FileNotFoundException ex)
            {
                context.getSource().sendError(new TranslatableText("wnt.toolbox.ashcon.error.player_not_found"));
            }
            catch (JsonParseException ex)
            {
                context.getSource().sendError(new TranslatableText("wnt.toolbox.ashcon.error.bad_json"));
            }
            catch (IllegalArgumentException ex)
            {
                context.getSource().sendError(new TranslatableText("wnt.toolbox.ashcon.error.not_a_uuid", args[0].toLowerCase()));
            }
            catch (Throwable ex)
            {
                context.getSource().sendError(new TranslatableText("wnt.toolbox.ashcon.error.unknown"));
                WNT.LOGGER.error("Details of the error: ", ex);
            }

            return null;
        }).whenComplete((result, ex) -> {
            if (result == null) return;

            context.getSource().sendFeedback(new TranslatableText("wnt.messages.command.name.result",
                    new LiteralText(result.getUuid()).formatted(Formatting.WHITE),
                    new LiteralText(result.getUsername()).setStyle(Style.EMPTY.withClickEvent(
                                    new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, result.getUsername())).withUnderline(true)
                            .withColor(TextColor.fromFormatting(Formatting.WHITE))))
                    .formatted(Formatting.GRAY));
        });

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return new ArrayList<>();
    }
}
