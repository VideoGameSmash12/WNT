package me.videogamesm12.wnt.overhauled_blackbox.window.tab;

import me.videogamesm12.wnt.overhauled_blackbox.Blackbox;
import me.videogamesm12.wnt.overhauled_blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.overhauled_blackbox.window.model.MapTableModel;
import me.videogamesm12.wnt.overhauled_blackbox.window.model.enhanced.EnhancedMapTableModel;

import javax.swing.*;
import java.awt.*;

public class MapsTab extends ScrollableTab
{
    private final JTable table;

    public MapsTab()
    {
        table = new JTable(Blackbox.getInstance().getConfig().isEnhancedListingEnabled() ?
                new EnhancedMapTableModel() : new MapTableModel());

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
