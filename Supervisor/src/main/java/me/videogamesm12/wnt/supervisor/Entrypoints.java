package me.videogamesm12.wnt.supervisor;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class Entrypoints implements PreLaunchEntrypoint, ClientModInitializer
{
    @Override
    public void onPreLaunch()
    {
        FantasiaSupervisor.setup();
    }

    @Override
    public void onInitializeClient()
    {
        FantasiaSupervisor.getInstance().getFlags().setGameStartedYet(true);
    }
}
