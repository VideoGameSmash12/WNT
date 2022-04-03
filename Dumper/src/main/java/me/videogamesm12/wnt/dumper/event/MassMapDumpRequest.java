package me.videogamesm12.wnt.dumper.event;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface MassMapDumpRequest
{
    Event<MassMapDumpRequest> EVENT = EventFactory.createArrayBacked(MassMapDumpRequest.class, (listeners) -> (source) ->
    {
        for (MassMapDumpRequest listener : listeners)
        {
            ActionResult result = listener.onMapDumpRequested(source);

            if (result != ActionResult.PASS)
            {
                return ActionResult.FAIL;
            }
        }

        return ActionResult.SUCCESS;
    });

    ActionResult onMapDumpRequested(FabricClientCommandSource source);
}
