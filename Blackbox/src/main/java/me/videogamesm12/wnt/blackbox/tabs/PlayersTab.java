/*
 * Copyright (c) 2022 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.videogamesm12.wnt.blackbox.tabs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * <h1>PlayersTab</h1>
 * Tab for players showing up in the in-game player list, complete with their display name, UUID, and actual name.
 */
public class PlayersTab extends JPanel implements SupervisorTab
{
    public JScrollPane pane = new JScrollPane();
    public JTable table = new JTable(new PlayersTableModel());

    public PlayersTab()
    {
        table.setCellSelectionEnabled(true);
        //--
        pane.setViewportView(table);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(pane, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE))
                            .addContainerGap()))));

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(pane, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE))
                                .addContainerGap()))));
    }

    /**
     * Returns a dataset formatted as an Object list from a player list entry.
     * @param entry PlayerListEntry
     * @return      List<String>
     */
    private static List<Object> playerToObjList(PlayerListEntry entry)
    {
        Text display = entry.getDisplayName();

        return Arrays.asList(display != null ? display.asString() : null, entry.getProfile().getName(), entry.getProfile().getId().toString(), entry.getLatency());
    }

    /**
     * Updates the table.
     */
    @Override
    public void update()
    {
        ((DynamicTableModel) table.getModel()).update();
    }

    /**
     * <h2>PlayersTableModel</h2>
     * The model used for the table in the Players tab.
     */
    public static class PlayersTableModel extends AbstractTableModel implements DynamicTableModel
    {
        private final List<String> columns = Arrays.asList("Display Name", "Name", "UUID", "Ping (ms)");
        private final List<List<Object>> rows = new ArrayList<>();

        @Override
        public String getColumnName(int column)
        {
            return columns.get(column);
        }

        @Override
        public int getRowCount()
        {
            return rows.size();
        }

        @Override
        public int getColumnCount()
        {
            return columns.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return rows.get(rowIndex).get(columnIndex);
        }

        @Override
        public void update()
        {
            if (MinecraftClient.getInstance().getNetworkHandler() == null)
            {
                return;
            }

            rows.clear();

            for (PlayerListEntry entry : MinecraftClient.getInstance().getNetworkHandler().getPlayerList())
            {
                rows.add(playerToObjList(entry));
            }

            fireTableDataChanged();
        }
    }
}
