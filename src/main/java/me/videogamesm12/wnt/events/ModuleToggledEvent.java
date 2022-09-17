package me.videogamesm12.wnt.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.videogamesm12.wnt.event.CustomEvent;
import me.videogamesm12.wnt.module.Module;

@Getter
@RequiredArgsConstructor
public class ModuleToggledEvent extends CustomEvent
{
    private final Class<? extends Module> moduleClass;

    private final Module module;

    private final boolean enabledNow;
}
