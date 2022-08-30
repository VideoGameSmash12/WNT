package me.videogamesm12.wnt.toolbox.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.module.ModuleManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;

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

        ModuleManager mm = WNT.MODULES;

        switch (args[0].toLowerCase())
        {
            case "info", "information" ->
            {
                if (args.length == 1)
                    return false;

                if (!mm.isModuleRegistered(args[1].toLowerCase()))
                {
                    context.getSource().sendError(new TranslatableText("wnt.toolbox.commands.wntmm.not_registered"));
                    return true;
                }

                Module module = mm.getModule(args[1].toLowerCase());
                MutableText status = module.isEnabled() ?
                        new TranslatableText("wnt.generic.enabled").formatted(Formatting.GREEN) :
                        new TranslatableText("wnt.generic.disabled").formatted(Formatting.RED);

                context.getSource().sendFeedback(new TranslatableText("wnt.toolbox.header",
                        new TranslatableText("wnt.toolbox.commands.wntmm.info.header",
                                new LiteralText(module.getMeta().name()).formatted(Formatting.WHITE))
                                .formatted(Formatting.GRAY)).formatted(Formatting.DARK_GRAY));
                context.getSource().sendFeedback(new TranslatableText("wnt.toolbox.commands.wntmm.info.description",
                        new LiteralText(module.getMeta().description()).formatted(Formatting.WHITE)).formatted(Formatting.GRAY));
                context.getSource().sendFeedback(new TranslatableText("wnt.toolbox.commands.wntmm.info.status",
                        status).formatted(Formatting.GRAY));

            }
            case "list" ->
            {
                context.getSource().sendFeedback(new TranslatableText("wnt.toolbox.commands.wntmm.list.registered",
                        new LiteralText(String.valueOf(mm.getModuleNames().size())).formatted(Formatting.WHITE)).formatted(Formatting.GRAY));

                MutableText list = new TranslatableText("wnt.toolbox.commands.wntmm.list").formatted(Formatting.GRAY);
                mm.getModuleNames().forEach(moduleName -> list.append(new LiteralText(moduleName).formatted(Formatting.WHITE).append(new LiteralText(", ").formatted(Formatting.GRAY))));

                context.getSource().sendFeedback(list);
            }
            case "status" ->
            {
                if (args.length == 1)
                    return false;

                if (!mm.isModuleRegistered(args[1].toLowerCase()))
                {
                    context.getSource().sendError(new TranslatableText("wnt.toolbox.commands.wntmm.not_registered"));
                    return true;
                }

                Module module = mm.getModule(args[1].toLowerCase());
                MutableText status = module.isEnabled() ?
                        new TranslatableText("wnt.generic.enabled").formatted(Formatting.GREEN) :
                        new TranslatableText("wnt.generic.disabled").formatted(Formatting.RED);

                MutableText text = new TranslatableText("wnt.toolbox.commands.wntmm.status",
                        new LiteralText(module.getClass().getSimpleName()).formatted(Formatting.WHITE),
                        status).formatted(Formatting.GRAY);

                context.getSource().sendFeedback(text);
            }
            case "toggle" ->
            {
                if (args.length == 1)
                    return false;

                if (!mm.isModuleRegistered(args[1].toLowerCase()))
                {
                    context.getSource().sendError(new TranslatableText("wnt.toolbox.commands.wntmm.not_registered"));
                    return true;
                }

                Module module = mm.getModule(args[1].toLowerCase());

                if (module.isEnabled())
                    module.disable();
                else
                    module.enable();

                context.getSource().sendFeedback(new TranslatableText("wnt.toolbox.commands.wntmm.toggled", new LiteralText(module.getClass().getSimpleName()).formatted(Formatting.DARK_GREEN)).formatted(Formatting.GREEN));
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
                    return WNT.MODULES.getModuleNames();
                }
            }
        }

        return null;
    }
}
