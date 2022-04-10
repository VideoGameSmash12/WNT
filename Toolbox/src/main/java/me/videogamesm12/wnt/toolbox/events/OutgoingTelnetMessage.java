package me.videogamesm12.wnt.toolbox.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public interface OutgoingTelnetMessage
{
    Event<OutgoingTelnetMessage> EVENT = EventFactory.createArrayBacked(OutgoingTelnetMessage.class, (listeners) -> (text) ->
    {
        Arrays.stream(listeners).forEach((listener) -> listener.onTelnetSent(text));
    });

    void onTelnetSent(String text);
}
