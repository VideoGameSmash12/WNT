package me.videogamesm12.wnt.dumper.event;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface MassEntityDumpRequest
{
    Event<MassEntityDumpRequest> EVENT = EventFactory.createArrayBacked(MassEntityDumpRequest.class, (listeners) -> (source) ->
    {
        for (MassEntityDumpRequest listener : listeners)
        {
            ActionResult result = listener.onEntityDumpRequested(source);

            if (result != ActionResult.PASS)
            {
                return ActionResult.FAIL;
            }
        }

        return ActionResult.SUCCESS;
    });

    ActionResult onEntityDumpRequested(FabricClientCommandSource source);
}
