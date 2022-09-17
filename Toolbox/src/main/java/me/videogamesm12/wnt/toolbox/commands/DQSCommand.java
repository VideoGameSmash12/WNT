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

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.module.ModuleNotEnabledException;
import me.videogamesm12.wnt.toolbox.data.QueriedBlockDataSet;
import me.videogamesm12.wnt.toolbox.data.QueriedEntityDataSet;
import me.videogamesm12.wnt.toolbox.mixin.KeyboardMixin;
import me.videogamesm12.wnt.toolbox.modules.DataQueryStorage;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

public class DQSCommand extends WCommand
{
    public DQSCommand()
    {
        super("dqs", "Manages the data saved by the DataQueryStorage module.",
                "/dqs <clear | copy <entity | block> <id> | stats>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args) throws ModuleNotEnabledException
    {
        DataQueryStorage module = WNT.getModuleManager().getModule(DataQueryStorage.class);

        if (!module.isEnabled())
            throw new ModuleNotEnabledException(module);

        if (args.length == 0)
            return false;

        switch (args[0].toLowerCase())
        {
            case "clear" ->
            {
                module.clearData();
                context.getSource().sendFeedback(new TranslatableText("wnt.toolbox.commands.dqs.cleared").formatted(Formatting.GREEN));
            }
            case "copy", "recopy" ->
            {
                if (args.length < 3)
                    return false;

                int id = Integer.parseInt(args[2]);
                switch (args[1].toLowerCase())
                {
                    case "entity" ->
                    {
                        QueriedEntityDataSet dataSet = module.getEntityDataSet(id);

                        if (dataSet != null)
                        {
                            ((KeyboardMixin.KBAccessor) MinecraftClient.getInstance().keyboard).invokeCopyEntity(
                                    dataSet.getEntityIdentifier(), dataSet.getPos(), dataSet.getNbt());
                            ((KeyboardMixin.KBAccessor) MinecraftClient.getInstance().keyboard).invokeDebugLog(
                                    new TranslatableText("debug.inspect.server.entity"));
                        }
                        else
                        {
                            context.getSource().sendError(new TranslatableText("wnt.toolbox.commands.dqs.not_found"));
                        }
                    }
                    case "block" ->
                    {
                        QueriedBlockDataSet dataSet = module.getBlockDataSet(id);

                        if (dataSet != null)
                        {
                            ((KeyboardMixin.KBAccessor) MinecraftClient.getInstance().keyboard).invokeCopyBlock(
                                    dataSet.getState(), dataSet.getPos(), dataSet.getNbt());
                            ((KeyboardMixin.KBAccessor) MinecraftClient.getInstance().keyboard).invokeDebugLog(
                                    new TranslatableText("debug.inspect.server.block"));
                        }
                        else
                        {
                            context.getSource().sendError(new TranslatableText("wnt.toolbox.commands.dqs.not_found"));
                        }
                    }
                    default ->
                    {
                        return false;
                    }
                }
            }
            case "stats" ->
            {
                context.getSource().sendFeedback(
                        new TranslatableText("wnt.toolbox.header",
                                new TranslatableText("wnt.toolbox.commands.dqs.stats.header")
                                        .formatted(Formatting.WHITE)).formatted(Formatting.DARK_GRAY));
                context.getSource().sendFeedback(
                        new TranslatableText("wnt.toolbox.commands.dqs.stats.blocks",
                                new LiteralText(String.valueOf(module.getBlockDataSets().size()))
                                        .formatted(Formatting.WHITE)).formatted(Formatting.GRAY));
                context.getSource().sendFeedback(
                        new TranslatableText("wnt.toolbox.commands.dqs.stats.entities",
                                new LiteralText(String.valueOf(module.getEntityDataSets().size()))
                                        .formatted(Formatting.WHITE)).formatted(Formatting.GRAY));
            }
            default ->
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length < 2)
            return Arrays.asList("clear", "copy", "stats");
        else
            if (args[0].equalsIgnoreCase("copy"))
                return Arrays.asList("block", "entity");

        return null;
    }
}
