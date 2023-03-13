package me.videogamesm12.wnt.dumper.events;

import lombok.Getter;
import lombok.Setter;
import me.videogamesm12.wnt.event.CustomEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RequestMapDumpEvent extends CustomEvent
{
    @Getter
    private Identifier requester;

    @Getter
    private final List<Integer> maps = new ArrayList<>();
    @Getter
    @Setter
    private RequestType requestType;

    public RequestMapDumpEvent(Identifier requester)
    {
        this.requester = requester;
        this.requestType = RequestType.MASS;
    }

    public RequestMapDumpEvent(Identifier requester, int map)
    {
        this.requester = requester;
        this.requestType = RequestType.SINGULAR;
        this.maps.add(map);
    }

    public RequestMapDumpEvent(Identifier requester, List<Integer> maps)
    {
        this.requester = requester;
        this.requestType = RequestType.MULTIPLE;
        this.maps.addAll(maps);
    }

    public enum RequestType
    {
        MASS,
        MULTIPLE,
        SINGULAR
    }
}
