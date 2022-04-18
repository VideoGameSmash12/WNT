package me.videogamesm12.wnt.blackbox.menus;

import javax.swing.*;

public abstract class ModMenu<T> extends JMenu
{
    private final Class<T> modClass;

    public ModMenu(String name, Class<T> mClass)
    {
        super(name);
        //--
        modClass = mClass;
    }

    public abstract T getModInstance();
}
