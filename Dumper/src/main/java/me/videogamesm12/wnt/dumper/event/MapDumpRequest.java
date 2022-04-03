package me.videogamesm12.wnt.dumper.event;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface MapDumpRequest
{
    Event<MapDumpRequest> EVENT = EventFactory.createArrayBacked(MapDumpRequest.class, (listeners) -> (id, source) ->
    {
        for (MapDumpRequest listener : listeners)
        {
            ActionResult result = listener.onMapDumpRequested(id, source);

            if (result != ActionResult.PASS)
            {
                return ActionResult.FAIL;
            }
        }

        return ActionResult.SUCCESS;
    });

    ActionResult onMapDumpRequested(int id, FabricClientCommandSource source);
}
