package me.videogamesm12.wnt.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.videogamesm12.wnt.event.CustomEvent;
import me.videogamesm12.wnt.module.Module;

@Getter
@RequiredArgsConstructor
public class ModuleUnregisteredEvent<T extends Module> extends CustomEvent
{
    private final Class<T> moduleClass;
}
