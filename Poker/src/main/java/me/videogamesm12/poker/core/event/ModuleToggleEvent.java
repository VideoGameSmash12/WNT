package me.videogamesm12.poker.core.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.videogamesm12.wnt.event.CustomEvent;

@RequiredArgsConstructor
public class ModuleToggleEvent<M> extends CustomEvent
{
    @Getter
    private final M module;

    @Getter
    private final boolean enabledNow;
}
