package me.videogamesm12.wnt.overhauled_blackbox.window.model;

import me.videogamesm12.wnt.overhauled_blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityTableModel extends AbstractTableModel implements Dynamic
{
    private final List<String> columns = Arrays.asList("Name", "Type", "Location", "ID", "UUID");
    private final List<List<String>> rows = new ArrayList<>();

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

        for (Entity entity : Supervisor.getInstance().getEntities())
        {
            rows.add(entityToStrList(entity));
        }

        fireTableDataChanged();
    }

    private static List<String> entityToStrList(Entity entity)
    {
        return Arrays.asList(entity.getDisplayName() != null ? entity.getDisplayName().getString() : entity.getEntityName(),
                EntityType.getId(entity.getType()).toString(),
                String.format("%s, %s, %s", entity.getX(), entity.getY(), entity.getZ()),
                String.valueOf(entity.getId()),
                entity.getUuidAsString());
    }
}