package me.videogamesm12.wnt.overhauled_blackbox.window.tab;

import me.videogamesm12.wnt.overhauled_blackbox.Blackbox;
import me.videogamesm12.wnt.overhauled_blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.overhauled_blackbox.window.model.PlayerTableModel;
import me.videogamesm12.wnt.overhauled_blackbox.window.model.enhanced.EnhancedPlayerTableModel;

import javax.swing.*;
import java.awt.*;

public class PlayersTab extends ScrollableTab
{
    private final JTable table;

    public PlayersTab()
    {
        table = new JTable(Blackbox.getInstance().getConfig().isEnhancedListingEnabled() ?
                new EnhancedPlayerTableModel() : new PlayerTableModel());
        table.setCellSelectionEnabled(true);
        setup();
    }

    @Override
    public Component getContentComponent()
    {
        return table;
    }

    @Override
    public void update()
    {
        EventQueue.invokeLater(() -> ((Dynamic) table.getModel()).update());
    }

}
