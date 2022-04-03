package me.videogamesm12.wnt.dumper.event;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
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

    ActionResult onEntityDumpRequested(FabricClientCommandSource source, int id);

    default void invoke(FabricClientCommandSource source, int id)
    {
        EVENT.invoker().onEntityDumpRequested(source, id);
    }
}
