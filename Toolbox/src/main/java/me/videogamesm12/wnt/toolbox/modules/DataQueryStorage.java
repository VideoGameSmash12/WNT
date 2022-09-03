package me.videogamesm12.wnt.toolbox.modules;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.module.ModuleMeta;
import me.videogamesm12.wnt.toolbox.Toolbox;
import me.videogamesm12.wnt.toolbox.data.QueriedBlockDataSet;
import me.videogamesm12.wnt.toolbox.data.QueriedEntityDataSet;
import me.videogamesm12.wnt.toolbox.event.client.CopyBlockToClipboard;
import me.videogamesm12.wnt.toolbox.event.client.CopyEntityToClipboard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@ModuleMeta(name = "DataQueryStorage", description = "Stores data collected by F3 + I")
public class DataQueryStorage extends Module
{
    @Getter
    private final List<QueriedEntityDataSet> entityDataSets = new ArrayList<>();
    @Getter
    private final List<QueriedBlockDataSet> blockDataSets = new ArrayList<>();

    @Override
    public void onStart()
    {
        Toolbox.getEventBus().register(this);
    }

    @Override
    public void onStop()
    {
        Toolbox.getEventBus().unregister(this);
    }

    @Nullable
    public QueriedEntityDataSet getEntityDataSet(int id)
    {
        if (id < 0 || entityDataSets.size() <= id)
            return null;
        else
            return entityDataSets.get(id);
    }

    @Nullable
    public QueriedBlockDataSet getBlockDataSet(int id)
    {
        if (id < 0 || blockDataSets.size() <= id)
            return null;
        else
            return blockDataSets.get(id);
    }

    public void clearData()
    {
        blockDataSets.clear();
        entityDataSets.clear();
    }

    @Subscribe
    public void onCopyEntityToClipboard(CopyEntityToClipboard event)
    {
        // If we're not enabled, don't do anything
        if (!isEnabled())
            return;

        // Get the dataset
        QueriedEntityDataSet dataSet = event.getDataSet();

        // Mark it to not be re-saved in the future when we do another query.
        dataSet.getNbt().putBoolean("wntToolbox.cached", true);

        // Done
        entityDataSets.add(dataSet);
    }

    @Subscribe
    public void onCopyBlockToClipboard(CopyBlockToClipboard event)
    {
        // If we're not enabled, don't do anything
        if (!isEnabled())
            return;

        // Get the dataset
        QueriedBlockDataSet dataSet = event.getDataSet();

        // Mark it to not be re-saved in the future when we do another query.
        dataSet.getNbt().putBoolean("wntToolbox.cached", true);

        // Done
        blockDataSets.add(dataSet);
    }
}
