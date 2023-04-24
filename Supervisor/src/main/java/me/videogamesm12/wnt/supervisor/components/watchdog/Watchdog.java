package me.videogamesm12.wnt.supervisor.components.watchdog;

import lombok.Getter;
import lombok.Setter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.supervisor.api.SVComponent;
import me.videogamesm12.wnt.supervisor.event.ClientFreezeDetected;
import net.kyori.adventure.key.Key;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Watchdog implements SVComponent
{
    @Getter
    @Setter
    private static long lastRenderedTime = 99999999L;
    //--
    private final Key identifier = Key.key("wnt", "watchdog");
    private final ScheduledExecutorService freezeDetector = new ScheduledThreadPoolExecutor(1);

    @Override
    public Key identifier()
    {
        return identifier;
    }

    @Override
    public void setup()
    {
        freezeDetector.scheduleAtFixedRate(() ->
        {
            // The client hasn't rendered something in 5 seconds. This usually indicates that the game has frozen.
            if (Instant.now().toEpochMilli() - getLastRenderedTime() >= 5000)
            {
                if (!ClientFreezeDetected.EVENT.invoker().onClientFreeze(getLastRenderedTime()).isAccepted())
                {
                    return;
                }

                WNT.getLogger().error("--== Supervisor has detected a client-side freeze! ==--");
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown()
    {
        freezeDetector.shutdownNow();
    }
}
