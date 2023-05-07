package me.videogamesm12.wnt.overhauled_blackbox.window.model;

import me.videogamesm12.wnt.overhauled_blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.map.MapState;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapTableModel extends AbstractTableModel implements Dynamic
{
    private final List<String> columns = Arrays.asList("ID", "Scale", "World");
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

        Supervisor.getInstance().getMaps().forEach((entry, lol) -> rows.add(mapToObjectList(entry, lol)));

        fireTableDataChanged();
    }

    public static List<Object> mapToObjectList(String id, MapState map)
    {
        return Arrays.asList(id,
                String.valueOf(map.scale),
                map.dimension.getValue().toString());
    }
}
