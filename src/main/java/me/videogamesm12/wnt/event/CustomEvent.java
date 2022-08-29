package me.videogamesm12.wnt.event;

import lombok.Getter;
import lombok.Setter;

public abstract class CustomEvent
{
    @Getter
    @Setter
    private boolean cancelled;
}
