package me.videogamesm12.wnt.dumper.events;

import lombok.Getter;
import lombok.Setter;
import me.videogamesm12.wnt.event.CustomEvent;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class RequestEntityDumpEvent<T> extends CustomEvent
{
    @Getter
    private final List<Entity> entities = new ArrayList<>();
    @Getter
    private final T source;
    @Getter
    @Setter
    private RequestType requestType;

    public RequestEntityDumpEvent(T source)
    {
        this.source = source;
        this.requestType = RequestType.MASS;
    }

    public RequestEntityDumpEvent(Entity entity, T source)
    {
        this.source = source;
        this.requestType = RequestType.SINGULAR;
        this.entities.add(entity);
    }

    public RequestEntityDumpEvent(List<Entity> entities, T source)
    {
        this.source = source;
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
