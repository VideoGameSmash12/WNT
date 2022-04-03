package me.videogamesm12.wnt.supervisor.event;

import me.videogamesm12.wnt.supervisor.networking.NetworkStorage;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public interface NetworkStorageCreated
{
    Event<NetworkStorageCreated> EVENT = EventFactory.createArrayBacked(NetworkStorageCreated.class, (listeners) -> (storage) ->
    {
        Arrays.stream(listeners).forEach((listener) -> listener.onStorageCreated(storage));
    });

    void onStorageCreated(NetworkStorage storage);
}
