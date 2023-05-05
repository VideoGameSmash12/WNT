package me.videogamesm12.wnt.overhauled_blackbox.window.tab;

import me.videogamesm12.wnt.overhauled_blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.overhauled_blackbox.window.model.EntityTableModel;

import javax.swing.*;
import java.awt.*;

public class EntitiesTab extends ScrollableTab
{
    public final JTable table;

    public EntitiesTab()
    {
        table = new JTable(new EntityTableModel());
        setup();
    }

    @Override
    public JComponent getContentComponent()
    {
        return table;
    }

    @Override
    public void update()
    {
        EventQueue.invokeLater(() -> ((Dynamic) table.getModel()).update());
    }
}
