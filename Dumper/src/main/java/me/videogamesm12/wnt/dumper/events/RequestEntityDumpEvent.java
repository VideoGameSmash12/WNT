package me.videogamesm12.wnt.dumper.events;

import lombok.Getter;
import lombok.Setter;
import me.videogamesm12.wnt.event.CustomEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RequestEntityDumpEvent extends CustomEvent
{
    @Getter
    private final Identifier requester;

    @Getter
    private final List<Entity> entities = new ArrayList<>();

    @Getter
    @Setter
    private RequestType requestType;

    public RequestEntityDumpEvent(Identifier requester)
    {
        this.requester = requester;
        this.requestType = RequestType.MASS;
    }

    public RequestEntityDumpEvent(Identifier requester, Entity entity)
    {
        this.requester = requester;
        this.requestType = RequestType.SINGULAR;
        this.entities.add(entity);
    }

    public RequestEntityDumpEvent(Identifier requester, List<Entity> entities)
    {
        this.requester = requester;
        this.requestType = RequestType.MULTIPLE;
        this.entities.addAll(entities);
    }

    public enum RequestType
    {
        MASS,
        MULTIPLE,
        SINGULAR
    }
}
