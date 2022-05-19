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
import net.minecraft.entity.Entity;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class EntityTab extends JPanel implements SupervisorTab
{
    public JScrollPane pane = new JScrollPane();
    public JTable table = new JTable(new EntityTableModel());

    public EntityTab()
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

    private static List<String> entityToStrList(Entity entity)
    {
        return Arrays.asList(entity.getDisplayName() != null ? entity.getDisplayName().getString() : entity.getEntityName(),
                String.format("%s, %s, %s", entity.getX(), entity.getY(), entity.getZ()), String.valueOf(entity.getId()),
                entity.getUuidAsString());
    }

    @Override
    public void update()
    {
        ((DynamicTableModel) table.getModel()).update();
    }

    public static class EntityTableModel extends AbstractTableModel implements DynamicTableModel
    {
        private final List<String> columns = Arrays.asList("Name", "Location", "ID", "UUID");
        private final List<List<String>> rows = new ArrayList<>();

        public EntityTableModel()
        {
        }

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
        public String getValueAt(int rowIndex, int columnIndex)
        {
            return rows.get(rowIndex).get(columnIndex);
        }

        @Override
        public void update()
        {
            if (MinecraftClient.getInstance().world == null)
            {
                return;
            }

            rows.clear();

            for (Entity entity : MinecraftClient.getInstance().world.getEntities())
            {
                rows.add(entityToStrList(entity));
            }

            fireTableDataChanged();
        }
    }
}
