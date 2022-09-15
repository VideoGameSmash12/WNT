package me.videogamesm12.poker.core.gui;

import me.videogamesm12.wnt.blackbox.menus.ModMenu;

import javax.swing.*;

public class PModMenu<T> extends ModMenu<T>
{
    private final T instance;

    public PModMenu(String name, T instance)
    {
        super(name, (Class<T>) instance.getClass());
        this.instance = instance;
    }

    @Override
    public T getModInstance()
    {
        return instance;
    }

    public void addSubMenu(PModSubMenu subMenu)
    {
        if (subMenu instanceof JMenu asMenu)
        {
            add(asMenu);
        }
    }
}
