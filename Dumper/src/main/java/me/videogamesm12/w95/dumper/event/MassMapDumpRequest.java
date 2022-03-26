package me.videogamesm12.w95.dumper.event;

import me.videogamesm12.w95.Notifiable;
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

    ActionResult onMapDumpRequested(Notifiable source);
}
