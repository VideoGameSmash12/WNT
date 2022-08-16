package me.videogamesm12.wnt.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public interface WNTInitializedEvent
{
    Event<WNTInitializedEvent> EVENT = EventFactory.createArrayBacked(WNTInitializedEvent.class, (listeners) -> () -> Arrays.stream(listeners).forEach(WNTInitializedEvent::onWNTInitialized));

    void onWNTInitialized();
}
