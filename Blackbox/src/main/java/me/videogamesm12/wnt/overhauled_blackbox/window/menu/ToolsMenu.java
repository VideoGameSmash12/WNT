package me.videogamesm12.wnt.overhauled_blackbox.window.menu;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.supervisor.Supervisor;

import javax.swing.*;

public class ToolsMenu extends JMenu
{
    public ToolsMenu()
    {
        super("Tools");
        //--
        JMenuItem dumpThreads = new JMenuItem("Dump thread information");
        dumpThreads.setToolTipText("Dumps current thread information to disk.");
        dumpThreads.addActionListener((e) -> Supervisor.getInstance().dumpThreads().forEach(line -> WNT.getLogger().info(line)));
        add(dumpThreads);
    }
}
