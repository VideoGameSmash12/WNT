package me.videogamesm12.wnt.event.module;

import me.videogamesm12.wnt.module.Module;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Arrays;

public interface ModuleEnabledEvent
{
    Event<ModuleEnabledEvent> EVENT = EventFactory.createArrayBacked(ModuleEnabledEvent.class, (listeners) -> (module) -> Arrays.stream(listeners).forEach(listener -> listener.onEnable(module)));

    void onEnable(Module module);
}
