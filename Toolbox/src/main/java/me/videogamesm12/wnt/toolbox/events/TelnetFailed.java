package me.videogamesm12.wnt.toolbox.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public interface TelnetFailed
{
    Event<TelnetFailed> EVENT = EventFactory.createArrayBacked(TelnetFailed.class, (listeners) -> (ex) ->
    {
        Arrays.stream(listeners).forEach((listener) -> listener.onConnectionFailure(ex));
    });

    void onConnectionFailure(Exception ex);
}
