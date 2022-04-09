package me.videogamesm12.wnt.blackbox.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.blackbox.SupervisorGUI;
import me.videogamesm12.wnt.command.WCommand;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BlackboxCommand extends WCommand
{
    public BlackboxCommand()
    {
        super("blackbox", "", "/blackbox <open | status>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "open":
            {
                SwingUtilities.invokeLater(() -> {
                    if (SupervisorGUI.GUI == null)
                    {
                        SupervisorGUI.GUI = new SupervisorGUI.GUIFrame();
                    }

                    SupervisorGUI.GUI.setVisible(true);
                });
                break;
            }
        }

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return new ArrayList<>();
    }
}
