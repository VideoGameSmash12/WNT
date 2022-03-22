package me.videogamesm12.w95.supervisorgui.menus;

import me.videogamesm12.w95.W95;
import me.videogamesm12.w95.module.WModule;

import javax.swing.*;

public class W95Menu extends JMenu
{
    public JMenu modulesMenu = new JMenu("Modules");

    public W95Menu()
    {
        W95.MODULES.getModules().forEach((module) -> {
            JMenu moduleMenu = new JMenu(module.getName());
            //--
            JCheckBoxMenuItem enabledMenu = new JCheckBoxMenuItem("Enabled");
            //--
            modulesMenu.add(moduleMenu);
        });
    }
    
    public static class ModuleMenu extends JMenu
    {
        public ModuleMenu(WModule module)
        {
            super(module.getName());


        }
    }
}
