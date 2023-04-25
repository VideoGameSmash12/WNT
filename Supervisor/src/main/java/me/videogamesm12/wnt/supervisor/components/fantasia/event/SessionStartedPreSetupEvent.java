package me.videogamesm12.wnt.supervisor.components.fantasia.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.videogamesm12.wnt.event.CustomEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.ISession;

@Getter
@RequiredArgsConstructor
public class SessionStartedPreSetupEvent extends CustomEvent
{
    private final ISession session;
}
