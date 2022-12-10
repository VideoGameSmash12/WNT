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

package me.videogamesm12.wnt.blackbox.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.blackbox.Blackbox;
import me.videogamesm12.wnt.command.WCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

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
                    if (Blackbox.GUI == null)
                    {
                        Blackbox.GUI = new Blackbox.GUIFrame();
                    }

                    Blackbox.GUI.setVisible(true);
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
