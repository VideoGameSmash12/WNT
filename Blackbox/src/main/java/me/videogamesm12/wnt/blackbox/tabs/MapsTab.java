package me.videogamesm12.wnt.blackbox.tabs;

import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.map.MapState;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsTab extends JPanel implements SupervisorTab
{
    public JScrollPane pane = new JScrollPane();
    public JTable table = new JTable(new MapTableModel());

    public MapsTab()
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

    @Override
    public void update()
    {
        ((DynamicTableModel) table.getModel()).update();
    }

    public static class MapTableModel extends AbstractTableModel implements DynamicTableModel
    {
        private final List<String> columns = Arrays.asList("ID", "Scale", "World"/*, "Image"*/);
        private final List<List<Object>> rows = new ArrayList<>();

        public MapTableModel()
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
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return rows.get(rowIndex).get(columnIndex);
        }

        /*@Override
        public Class getColumnClass(int column)
        {
            return getValueAt(0, column).getClass();
        }*/

        @Override
        public void update()
        {
            if (MinecraftClient.getInstance().world == null)
            {
                return;
            }

            rows.clear();

            for (String id : ((ClientWorldMixin) MinecraftClient.getInstance().world).getMapStates().keySet())
            {
                rows.add(mapToList(id, MinecraftClient.getInstance().world.getMapState(id)));
            }

            fireTableDataChanged();
        }
    }

    public static List<Object> mapToList(String id, MapState map)
    {
        return Arrays.asList(id, String.valueOf(map.scale), map.dimension.getValue().toString()/*, new ImageIcon(map.colors).getImage()*/);
    }
}
