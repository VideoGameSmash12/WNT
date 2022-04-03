package me.videogamesm12.wnt.supervisor.networking;

import lombok.Getter;

/**
 * <h1>PacketType</h1>
 * Packet type metadata storage for
 */
public enum PacketType
{
    S2C_LOGIN_COMPRESSION("LoginCompressionS2CPacket", "Setting up packet compression...", NetworkDirection.S2C),
    S2C_LOGIN_DISCONNECTED("LoginDisconnectS2CPacket", "Something isn't right - disconnect!", NetworkDirection.S2C),
    S2C_LOGIN_HELLO("LoginHelloS2CPacket", "Setting up packet encryption...", NetworkDirection.S2C),
    S2C_LOGIN_QUERY("LoginQueryRequestS2CPacket", "Do you understand what this means?", NetworkDirection.S2C),
    S2C_LOGIN_SUCCESS("LoginSuccessS2CPacket", "Authentication complete! Joining game...", NetworkDirection.S2C),
    //--
    S2C_PLAY_BLOCK_UPDATE("BlockUpdateS2CPacket", "A block has been updated.", NetworkDirection.S2C, false),
    S2C_PLAY_CHUNK_DATA("ChunkDataS2CPacket", "Chunk data with block entities.", NetworkDirection.S2C),
    S2C_PLAY_ENTITY_SPAWN("EntitySpawnS2CPacket", "An entity spawned in nearby.", NetworkDirection.S2C),
    S2C_PLAY_EXPLOSION("ExplosionS2CPacket", "BOOM! Something blew up.", NetworkDirection.S2C),
    S2C_PLAY_LIGHT_UPDATE("LightUpdateS2CPacket", "The lighting for a chunk has changed.", NetworkDirection.S2C),
    S2C_PLAY_UNLOAD_CHUNK("UnloadChunkS2CPacket", "Unload a chunk from memory.", NetworkDirection.S2C),
    //--
    UNKNOWN("Unrecognized", "Unrecognized packet", NetworkDirection.UNKNOWN);

    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final NetworkDirection direction;
    @Getter
    private final boolean deserializable;

    PacketType(String name, String description, NetworkDirection direction)
    {
        this(name, description, direction, true);
    }

    PacketType(String name, String description, NetworkDirection direction, boolean deserializable)
    {
        this.name = name;
        this.description = description;
        this.direction = direction;
        this.deserializable = deserializable;
    }

    public enum NetworkDirection
    {
        S2C, C2S, UNKNOWN
    }
}
