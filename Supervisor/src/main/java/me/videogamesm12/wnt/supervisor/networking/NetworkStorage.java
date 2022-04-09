package me.videogamesm12.wnt.supervisor.networking;

import com.google.gson.*;
import lombok.Builder;
import lombok.Data;
import me.videogamesm12.wnt.supervisor.event.NetworkStorageCreated;
import net.minecraft.network.Packet;

import java.util.Arrays;
import java.util.List;

@Builder
@Data
public class NetworkStorage
{
    private static Gson gson = new Gson();

    private final Packet<?> packet;
    private final PacketType type;
    private boolean cancelled;

    public void invokeEvent()
    {
        NetworkStorageCreated.EVENT.invoker().onStorageCreated(this);
    }

    public List<Object> toList()
    {
        return Arrays.asList(type.getName(), type.getDescription(), cancelled, type.isDeserializable() ?
                getPacketAsJson() : getPacketSpecialJson());
    }

    public String getPacketAsJson()
    {
        return gson.toJson(packet);
    }

    public String getPacketSpecialJson()
    {
        JsonObject element = new JsonObject();
        element.addProperty("__special", true);
        element.addProperty("__type", type.getName());

        return gson.toJson(element);
    }
}
