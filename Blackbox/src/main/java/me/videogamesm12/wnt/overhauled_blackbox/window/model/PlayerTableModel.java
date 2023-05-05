package me.videogamesm12.wnt.overhauled_blackbox.window.model;

import me.videogamesm12.wnt.overhauled_blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTableModel extends AbstractTableModel implements Dynamic
{
    private final List<String> columns = Arrays.asList("Username", "Display Name",  "UUID", "Ping (ms)");
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
        if (MinecraftClient.getInstance().world == null)
        {
            return;
        }

        rows.clear();

        for (PlayerListEntry entry : Supervisor.getInstance().getPlayerList())
        {
            rows.add(playerToObjList(entry));
        }

        fireTableDataChanged();
    }

    private static List<Object> playerToObjList(PlayerListEntry entry)
    {
        return Arrays.asList(
                entry.getProfile().getName(),                                               // Username
                entry.getDisplayName() != null ? entry.getDisplayName().getString() : null, // Display Name
                entry.getProfile().getId(),                                                 // UUID
                entry.getLatency()                                                          // Ping
        );
    }
}
