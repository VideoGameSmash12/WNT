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
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.module.ModuleManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WNTMMCommand extends WCommand
{
    public WNTMMCommand()
    {
        super("wntmm", "Control modules with a single command", "/wntmm <list | info [module] | status [module] | toggle [module]>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
            return false;

        ModuleManager mm = WNT.getModuleManager();

        switch (args[0].toLowerCase())
        {
            case "info", "information" ->
            {
                if (args.length == 1)
                    return false;

                if (!mm.isModuleRegistered(args[1].toLowerCase()))
                {
                    msg(Component.translatable("wnt.toolbox.commands.wntmm.not_registered").color(NamedTextColor.RED));
                    return true;
                }

                Module module = mm.getModule(args[1].toLowerCase());
                Component status = module.isEnabled() ?
                        Component.translatable("wnt.generic.enabled", NamedTextColor.GREEN) :
                        Component.translatable("wnt.generic.disabled", NamedTextColor.RED);

                msg(Component.translatable("wnt.toolbox.header",
                        Component.translatable("wnt.toolbox.commands.wntmm.info.header",
                                Component.text(module.getMeta().name()).color(NamedTextColor.WHITE))
                                .color(NamedTextColor.GRAY))
                        .color(NamedTextColor.DARK_GRAY));

                msg(Component.translatable("wnt.toolbox.commands.wntmm.info.description",
                        Component.text(module.getMeta().description()).color(NamedTextColor.WHITE))
                        .color(NamedTextColor.GRAY));

                msg(Component.translatable("wnt.toolbox.commands.wntmm.info.status", status));
            }
            case "list" ->
            {
                msg(Component.translatable("wnt.toolbox.commands.wntmm.list.registered",
                        Component.text(mm.getModuleNames().size()).color(NamedTextColor.WHITE))
                        .color(NamedTextColor.GRAY));

                TranslatableComponent.Builder builder = Component.translatable("wnt.toolbox.commands.wntmm.list", NamedTextColor.GRAY).toBuilder();

                // This is a stupid way of doing this, but I don't really have a choice. I tried replacing this with a
                // Component.join call, but for whatever reason, I couldn't get it to actually work properly.
                // It would just show a blank component instead of the list.
                AtomicInteger number = new AtomicInteger(1);
                mm.getModules().values().forEach(module -> {
                    builder.append(Component.text(module.getMeta().name())
                            .color(module.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED)
                            .hoverEvent(HoverEvent.showText(Component.text(module.getMeta().name())
                                    .decorate(TextDecoration.BOLD)
                                    .append(Component.text("\n"))
                                    .append(Component.text(module.getMeta().description())
                                            .color(NamedTextColor.GRAY)
                                            .decoration(TextDecoration.BOLD, false)))));

                    // If there is more to come, then append ", " at the end
                    if (number.getAndIncrement() < mm.getModuleNames().size())
                        builder.append(Component.text(", ").color(NamedTextColor.GRAY));
                });

                msg(builder.build());
            }
            case "status" ->
            {
                if (args.length == 1)
                    return false;

                if (!mm.isModuleRegistered(args[1].toLowerCase()))
                {
                    msg(Component.translatable("wnt.toolbox.commands.wntmm.not_registered").color(NamedTextColor.RED));
                    return true;
                }

                Module module = mm.getModule(args[1].toLowerCase());
                Component status = module.isEnabled() ?
                        Component.translatable("wnt.generic.enabled", NamedTextColor.GREEN) :
                        Component.translatable("wnt.generic.disabled", NamedTextColor.RED);

                msg(Component.translatable("wnt.toolbox.commands.wntmm.status",
                        Component.text(module.getMeta().name())
                                .color(NamedTextColor.WHITE),
                                status)
                        .color(NamedTextColor.GRAY));
            }
            case "toggle" ->
            {
                if (args.length == 1)
                    return false;

                if (!mm.isModuleRegistered(args[1].toLowerCase()))
                {
                    msg(Component.translatable("wnt.toolbox.commands.wntmm.not_registered").color(NamedTextColor.RED));
                    return true;
                }

                Module module = mm.getModule(args[1].toLowerCase());

                if (module.isEnabled())
                    module.disable();
                else
                    module.enable();

                msg(Component.translatable("wnt.toolbox.commands.wntmm.toggled",
                        Component.text(module.getMeta().name())
                                .color(NamedTextColor.DARK_GREEN))
                        .color(NamedTextColor.GREEN));
            }
            default -> {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        switch (args.length)
        {
            case 0, 1 -> {
                return Arrays.asList("info", "list", "status", "toggle");
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("info"))
                {
                    return WNT.getModuleManager().getModuleNames();
                }
            }
        }

        return null;
    }
}
