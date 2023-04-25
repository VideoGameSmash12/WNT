package me.videogamesm12.wnt.supervisor;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.MinecraftClient;

public class Entrypoints implements PreLaunchEntrypoint, ClientModInitializer, ClientLifecycleEvents.ClientStopping
{
    @Override
    public void onPreLaunch()
    {
        Supervisor.setup();
    }

    @Override
    public void onInitializeClient()
    {
        Supervisor.getInstance().getFlags().setGameStartedYet(true);
        ClientLifecycleEvents.CLIENT_STOPPING.register(this);
    }

    @Override
    public void onClientStopping(MinecraftClient client)
    {
        Supervisor.getInstance().shutdown();
    }
}
