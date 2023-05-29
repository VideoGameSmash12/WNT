package me.videogamesm12.wnt.blackbox.window.model;

import me.videogamesm12.wnt.blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.enums.InventoryType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryTableModel extends AbstractTableModel implements Dynamic
{
    private final List<String> columns = Arrays.asList("Name", "Type", "Count", "Location");
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
        if (MinecraftClient.getInstance().player == null)
        {
            return;
        }

        rows.clear();

        Supervisor.getInstance().getInventory().forEach((type, entry) -> entry.stream().filter(item ->
        {
            final RegistryEntry<Item> key = item.getRegistryEntry();
            return key.getKey().isPresent() && !key.getKey().get().getValue().equals(new Identifier("minecraft", "air"));
        }).forEach(item -> rows.add(itemToObjectList(type, item))));

        fireTableDataChanged();
    }

    public static List<Object> itemToObjectList(InventoryType type, ItemStack stack)
    {
        return Arrays.asList(stack.getName().getString(),
                stack.getRegistryEntry().getKey().get().getValue().toString(),
                stack.getCount(),
                StringUtils.capitalize(type.name().toLowerCase()));
    }
}