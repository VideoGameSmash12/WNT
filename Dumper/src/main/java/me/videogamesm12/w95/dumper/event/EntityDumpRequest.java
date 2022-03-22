package me.videogamesm12.w95.dumper.event;

import me.videogamesm12.w95.Notifiable;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface EntityDumpRequest
{
    Event<EntityDumpRequest> EVENT = EventFactory.createArrayBacked(EntityDumpRequest.class, (listeners) -> (source, id) ->
    {
        for (EntityDumpRequest listener : listeners)
        {
            ActionResult result = listener.onEntityDumpRequested(source, id);

            if (result != ActionResult.PASS)
            {
                return ActionResult.FAIL;
            }
        }

        return ActionResult.SUCCESS;
    });

    ActionResult onEntityDumpRequested (Notifiable source, int id);

    default void invoke(Notifiable source, int id)
    {
        EVENT.invoker().onEntityDumpRequested(source, id);
    }
}
