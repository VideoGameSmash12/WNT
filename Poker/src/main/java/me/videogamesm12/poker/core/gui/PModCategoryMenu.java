package me.videogamesm12.poker.core.gui;

import javax.swing.*;
import java.util.Collection;

public class PModCategoryMenu extends JMenu implements PModSubMenu
{
    public PModCategoryMenu(String name, Collection<PModModuleMenu<?>> modules)
    {
        this(name);
        addModules(modules);
    }

    public PModCategoryMenu(String name)
    {
        super(name);
    }

    public void addModule(PModModuleMenu<?> module)
    {
        add(module);
    }

    public void addModules(Collection<PModModuleMenu<?>> modules)
    {
        modules.forEach(this::addModule);
    }

    @Override
    public void addSubMenu(PModSubMenu menu)
    {
        if (menu instanceof JMenu jMenu) add(jMenu);
    }
}
