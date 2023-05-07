package me.videogamesm12.wnt.blackbox.window.menu;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.Blackbox;
import me.videogamesm12.wnt.supervisor.Supervisor;

import javax.swing.*;

public class ToolsMenu extends JMenu
{
    public ToolsMenu()
    {
        super("Tools");
        //--
        final JMenuItem chatWindow = new JMenuItem("Open Console");
        chatWindow.setToolTipText("Opens the Blackbox Console.");
        chatWindow.addActionListener((e) -> Blackbox.getInstance().getMainWindow().openConsoleWindow());
        add(chatWindow);
        //--
        final JMenuItem dumpThreads = new JMenuItem("Dump thread information");
        dumpThreads.setToolTipText("Dumps current thread information to disk.");
        dumpThreads.addActionListener((e) -> Supervisor.getInstance().dumpThreads().forEach(line -> WNT.getLogger().info(line)));
        add(dumpThreads);
    }
}
