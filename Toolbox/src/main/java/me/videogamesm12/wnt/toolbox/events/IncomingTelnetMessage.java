package me.videogamesm12.wnt.toolbox.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public interface IncomingTelnetMessage
{
    Event<IncomingTelnetMessage> EVENT = EventFactory.createArrayBacked(IncomingTelnetMessage.class, (listeners) -> (text) ->
    {
        Arrays.stream(listeners).forEach((listener) -> listener.onTelnetMessage(text));
    });

    void onTelnetMessage(String text);
}
