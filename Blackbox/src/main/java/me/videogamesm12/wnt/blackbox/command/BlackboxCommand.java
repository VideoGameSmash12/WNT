package me.videogamesm12.wnt.blackbox.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.module.ModuleNotEnabledException;
import me.videogamesm12.wnt.blackbox.Blackbox;
import me.videogamesm12.wnt.supervisor.components.fantasia.command.FCommand;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public interface BlackboxCommand
{
    class Minecraft extends WCommand
    {
        public Minecraft()
        {
            super("blackbox", "Allows you to open or query the status of the Blackbox.", "/blackbox <open | status>");
        }

        @Override
        public boolean run(CommandContext<FabricClientCommandSource> context, String[] args) throws ModuleNotEnabledException
        {
            if (args.length == 0)
            {
                return false;
            }

            switch (args[0].toLowerCase())
            {
                case "open" ->
                {
                    msg(Component.translatable("wnt.blackbox.command.show", NamedTextColor.GREEN));
                    SwingUtilities.invokeLater(() -> Blackbox.getInstance().openWindow());
                }
                case "status" ->
                {
                    Component status = (Blackbox.getInstance().getMainWindow() != null ? (Blackbox.getInstance().getMainWindow().isVisible() ?
                            Component.translatable("wnt.blackbox.command.status.inMemoryAndVisible")
                            : Component.translatable("wnt.blackbox.command.status.inMemoryButNotVisible"))
                            : Component.translatable("wnt.blackbox.command.status.notInMemory")).color(NamedTextColor.WHITE);

                    msg(Component.translatable("wnt.blackbox.command.status", status).colorIfAbsent(NamedTextColor.GRAY));
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
            return Arrays.asList("open", "status");
        }
    }

    class Fantasia extends FCommand
    {
        public Fantasia()
        {
            super("blackbox", "Allows you to open, query the status of, or get a brief overview about the Blackbox.", "blackbox <about | open>");
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
                case "about" ->
                {
                    sender.sendMessage("The Blackbox is a part of WNT that allows you to control the Supervisor through a graphical user interface, or GUI for short.");
                    sender.sendMessage("It's still in the works, as it has a tendency to be somewhat unstable. However, it is (for the most part) functional.");
                    sender.sendMessage("You can access it with 'blackbox open'.");
                }
                case "open" ->
                {
                    sender.sendMessage("Opening the Blackbox...");
                    Blackbox.getInstance().openWindow();
                }
                case "status" ->
                {
                    sender.sendMessage("The Blackbox is currently " + (Blackbox.getInstance().getMainWindow() != null ?
                            Blackbox.getInstance().getMainWindow().isVisible() ? "in memory and open" : "in memory, but not visible" : "not in memory") + ".");
                }
                default ->
                {
                    return false;
                }
            }

            return true;
        }
    }
}
