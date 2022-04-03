package me.videogamesm12.wnt.blackbox.tabs;

import me.videogamesm12.wnt.blackbox.SupervisorGUI;
import me.videogamesm12.wnt.supervisor.networking.NetworkStorage;
import me.videogamesm12.wnt.supervisor.event.NetworkStorageCreated;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketsTab extends JPanel implements SupervisorTab
{
    public JScrollPane pane = new JScrollPane();
    public JTable table = new JTable(new PacketTableModel());

    public PacketsTab()
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
        // This isn't dynamic, so we're going to do nothing here.
    }

    public static class PacketTableModel extends AbstractTableModel implements DynamicTableModel, NetworkStorageCreated, ClientLoginConnectionEvents.Init
    {
        private final List<String> columns = Arrays.asList("Name", "Description", "Cancelled", "Data");
        private final List<List<Object>> rows = new ArrayList<>();

        public PacketTableModel()
        {
            NetworkStorageCreated.EVENT.register(this);
            ClientLoginConnectionEvents.INIT.register(this);
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
        }

        @Override
        public void onStorageCreated(NetworkStorage storage)
        {
            /*if (!SupervisorGUI.CONFIG.getBlacklistedTypes().contains(storage.getType()))
            {
                rows.add(storage.toList());
                fireTableDataChanged();
            }*/
        }

        @Override
        public void onLoginStart(ClientLoginNetworkHandler handler, MinecraftClient client)
        {
            /*rows.clear();
            fireTableDataChanged();*/
        }
    }
}
