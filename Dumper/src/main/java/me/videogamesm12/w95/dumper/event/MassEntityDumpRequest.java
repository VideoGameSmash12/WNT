package me.videogamesm12.w95.dumper.event;

import me.videogamesm12.w95.Notifiable;
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

    ActionResult onEntityDumpRequested(Notifiable source);
}
