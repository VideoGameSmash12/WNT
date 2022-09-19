package me.videogamesm12.wnt.dumper.events;

import lombok.Getter;
import lombok.Setter;
import me.videogamesm12.wnt.event.CustomEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.map.MapState;

import java.util.ArrayList;
import java.util.List;

public class RequestMapDumpEvent extends CustomEvent
{
    @Getter
    private final List<Integer> maps = new ArrayList<>();
    @Getter
    @Setter
    private RequestType requestType;

    public RequestMapDumpEvent()
    {
        this.requestType = RequestType.MASS;
    }

    public RequestMapDumpEvent(int map)
    {
        this.requestType = RequestType.SINGULAR;
        this.maps.add(map);
    }

    public RequestMapDumpEvent(List<Integer> maps)
    {
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
