package me.videogamesm12.wnt.supervisor.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.UUID;

public interface HUDMessageAdded
{
    Event<HUDMessageAdded> EVENT = EventFactory.createArrayBacked(HUDMessageAdded.class, (listeners) -> (type, message, sender) ->
    {
        for (HUDMessageAdded listener : listeners)
        {
            ActionResult result = listener.onMessageAdded(type, message, sender);

            if (result != ActionResult.PASS)
            {
                return ActionResult.FAIL;
            }
        }

        return ActionResult.SUCCESS;
    });

    ActionResult onMessageAdded(MessageType type, Text message, UUID sender);
}
