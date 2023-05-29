package me.videogamesm12.wnt.blackbox.window.tab;

import me.videogamesm12.wnt.blackbox.window.model.EntityTableModel;
import me.videogamesm12.wnt.blackbox.window.general.Dynamic;

import javax.swing.*;

public class EntitiesTab extends ScrollableTab
{
    public final JTable table;

    public EntitiesTab()
    {
        table = new JTable(new EntityTableModel());
        table.setCellSelectionEnabled(true);
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
        ((Dynamic) table.getModel()).update();
    }
}
