package me.videogamesm12.wnt.blackbox.window.tab;

import me.videogamesm12.wnt.blackbox.Blackbox;
import me.videogamesm12.wnt.blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.blackbox.window.model.InventoryTableModel;
import me.videogamesm12.wnt.blackbox.window.model.enhanced.EnhancedInventoryTableModel;

import javax.swing.*;
import java.awt.*;

public class InventoryTab extends ScrollableTab
{
    private final JTable table;

    public InventoryTab()
    {
        this.table = new JTable(Blackbox.getInstance().getConfig().isEnhancedListingEnabled() ?
                new EnhancedInventoryTableModel() : new InventoryTableModel());
        table.setCellSelectionEnabled(true);

        setup();
    }

    @Override
    public void update()
    {
        ((Dynamic) table.getModel()).update();
    }

    @Override
    public Component getContentComponent()
    {
        return table;
    }
}
