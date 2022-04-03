package me.videogamesm12.wnt.supervisor.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface ClientFreezeDetected
{
    Event<ClientFreezeDetected> EVENT = EventFactory.createArrayBacked(ClientFreezeDetected.class, (listeners) -> (time) ->
    {
        for (ClientFreezeDetected listener : listeners)
        {
            ActionResult result = listener.onClientFreeze(time);

            if (result != ActionResult.PASS)
            {
                return ActionResult.FAIL;
            }
        }

        return ActionResult.SUCCESS;
    });

    ActionResult onClientFreeze(long lastRender);
}
