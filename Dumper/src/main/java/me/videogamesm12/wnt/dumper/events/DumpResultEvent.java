package me.videogamesm12.wnt.dumper.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.videogamesm12.wnt.event.CustomEvent;
import net.kyori.adventure.text.Component;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

@AllArgsConstructor
@Getter
public class DumpResultEvent extends CustomEvent
{
    private final Identifier requester;

    private final ActionResult result;

    private final Component message;
}
