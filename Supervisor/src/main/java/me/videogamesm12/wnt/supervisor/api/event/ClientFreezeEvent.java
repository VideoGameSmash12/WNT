package me.videogamesm12.wnt.supervisor.api.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.videogamesm12.wnt.event.CustomEvent;

@Getter
@RequiredArgsConstructor
public class ClientFreezeEvent extends CustomEvent
{
    private final long lastRendered;
}
