package me.videogamesm12.wnt.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

/**
 * <h1>Messenger</h1>
 * My way of dealing with messages across various versions of Minecraft.
 */
public class Messenger
{
    private static final GsonComponentSerializer kyori = GsonComponentSerializer.builder().build();

    public static void sendChatMessage(Component component)
    {
        ClientPlayerEntity entity = MinecraftClient.getInstance().player;

        if (entity != null)
            entity.sendMessage(Text.Serializer.fromJson(kyori.serialize(component)), false);
    }

    public static Text convert(Component text)
    {
        return Text.Serializer.fromJson(kyori.serialize(text));
    }
}
