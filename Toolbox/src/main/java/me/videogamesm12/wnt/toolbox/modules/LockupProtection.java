package me.videogamesm12.wnt.toolbox.modules;

import com.google.common.eventbus.Subscribe;
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.toolbox.Toolbox;
import me.videogamesm12.wnt.toolbox.event.network.S2COpenScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandlerType;

public class LockupProtection extends Module
{
    @Override
    public void onStart()
    {
        Toolbox.getEventBus().register(this);
    }

    @Override
    public void onStop()
    {
        Toolbox.getEventBus().unregister(this);
    }

    @Subscribe
    public void onS2COpenScreen(S2COpenScreen event)
    {
        if (!isEnabled() || MinecraftClient.getInstance().player == null)
            return;

        if (event.getPacket().getScreenHandlerType() == ScreenHandlerType.GENERIC_9X4)
        {
            event.setCancelled(true);
        }
    }
}
