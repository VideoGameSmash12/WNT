package me.videogamesm12.wnt.blackbox.tools.chat;

import javax.swing.*;

public abstract class ChatWindowTab<T> extends JPanel
{
    private final Class<T> modClass;
    private final String tabName;

    public ChatWindowTab(String tabName, Class<T> modClass)
    {
        this.tabName = tabName;
        this.modClass = modClass;
    }

    public abstract String getTabName();

    public abstract T getModInstance();
}