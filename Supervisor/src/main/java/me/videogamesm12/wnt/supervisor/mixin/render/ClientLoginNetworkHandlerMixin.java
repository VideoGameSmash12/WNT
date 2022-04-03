package me.videogamesm12.wnt.supervisor.mixin.render;

import me.videogamesm12.wnt.supervisor.networking.NetworkStorage;
import me.videogamesm12.wnt.supervisor.networking.PacketType;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public class ClientLoginNetworkHandlerMixin
{
    @Inject(method = "onCompression", at = @At("HEAD"))
    public void onCompression(LoginCompressionS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_LOGIN_COMPRESSION)
                .build()
                .invokeEvent();
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void onDisconnect(LoginDisconnectS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_LOGIN_DISCONNECTED)
                .build()
                .invokeEvent();
    }

    @Inject(method = "onHello", at = @At("HEAD"))
    public void onHello(LoginHelloS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_LOGIN_HELLO)
                .build()
                .invokeEvent();
    }

    @Inject(method = "onLoginSuccess", at = @At("HEAD"))
    public void onLoginSuccess(LoginSuccessS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_LOGIN_SUCCESS)
                .build()
                .invokeEvent();
    }

    @Inject(method = "onQueryRequest", at = @At("HEAD"))
    public void onQueryRequest(LoginQueryRequestS2CPacket packet, CallbackInfo ci)
    {
        NetworkStorage.builder()
                .packet(packet)
                .type(PacketType.S2C_LOGIN_QUERY)
                .build()
                .invokeEvent();
    }
}
