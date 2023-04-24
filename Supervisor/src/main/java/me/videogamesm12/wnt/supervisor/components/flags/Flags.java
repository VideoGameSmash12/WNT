package me.videogamesm12.wnt.supervisor.components.flags;

import lombok.Getter;
import lombok.Setter;

public class Flags
{
    @Getter
    @Setter
    private boolean gameStartedYet;

    @Getter
    @Setter
    private boolean supposedToCrash;
}
