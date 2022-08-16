package me.videogamesm12.wnt.event.module;

import me.videogamesm12.wnt.module.Module;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public interface ModuleDisabledEvent
{
    Event<ModuleDisabledEvent> EVENT = EventFactory.createArrayBacked(ModuleDisabledEvent.class, (listeners) -> (module) -> Arrays.stream(listeners).forEach(listener -> listener.onDisable(module)));

    void onDisable(Module module);
}
