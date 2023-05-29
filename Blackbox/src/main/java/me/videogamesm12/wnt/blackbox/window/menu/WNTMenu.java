package me.videogamesm12.wnt.blackbox.window.menu;

import me.videogamesm12.wnt.blackbox.window.menu.wnt.DumperMenu;
import me.videogamesm12.wnt.blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.blackbox.window.menu.wnt.ModMenu;
import me.videogamesm12.wnt.blackbox.window.menu.wnt.ModulesMenu;

import javax.swing.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WNTMenu extends JMenu implements Dynamic
{
    private static final Queue<ModMenu<?>> modMenus = new ConcurrentLinkedQueue<>();
    //--
    private final JMenu hooksMenu = new JMenu("Hooks");

    public WNTMenu()
    {
        super("WNT");
        //--
        add(new ModulesMenu());
        add(new DumperMenu());
        add(hooksMenu);
    }

    @Override
    public void update()
    {
        for (int i = 0; i < modMenus.size(); i++)
        {
            hooksMenu.add(modMenus.poll());
        }
    }

    public static void queueModMenu(ModMenu<?> mod)
    {
        modMenus.add(mod);
    }
}
