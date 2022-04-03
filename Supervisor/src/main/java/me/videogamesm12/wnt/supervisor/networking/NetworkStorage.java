package me.videogamesm12.wnt.supervisor.networking;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import lombok.Builder;
import lombok.Data;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.supervisor.event.NetworkStorageCreated;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

import java.util.Arrays;
import java.util.List;

@Builder
@Data
public class NetworkStorage
{
    private static Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy()
    {
        private List<Class<?>> exclude = Arrays.asList(
                Block.class,
                AbstractBlock.AbstractBlockState.class,
                Codec.class);

        @Override
        public boolean shouldSkipField(FieldAttributes f)
        {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz)
        {
            WNT.LOGGER.info(clazz.getName());
            return exclude.contains(clazz);
        }
    }).create();

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

        switch (type)
        {
            case S2C_PLAY_BLOCK_UPDATE:
            {
                element.addProperty("blockType", ((BlockUpdateS2CPacket) packet).getState().getBlock().toString());
                element.addProperty("location", ((BlockUpdateS2CPacket) packet).getPos().toShortString());
                break;
            }

            case S2C_PLAY_ENTITY_SPAWN:
            {
                EntitySpawnS2CPacket paket = (EntitySpawnS2CPacket) packet;


                element.addProperty("", ((BlockUpdateS2CPacket) packet).getState().getBlock().toString());
                element.addProperty("location", ((BlockUpdateS2CPacket) packet).getPos().toShortString());
                break;
            }
        }

        return gson.toJson(element);
    }
}
