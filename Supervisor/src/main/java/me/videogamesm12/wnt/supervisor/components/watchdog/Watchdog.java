package me.videogamesm12.wnt.supervisor.components.watchdog;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.api.SVComponent;
import me.videogamesm12.wnt.supervisor.api.event.ClientFreezeEvent;
import net.kyori.adventure.key.Key;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Watchdog extends Thread implements SVComponent
{
    public static long LAST_RENDERED_TIME = 0L;
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
        start();
    }

    @Override
    public void run()
    {
        freezeDetector.scheduleAtFixedRate(() ->
        {
            // Has the game even started up yet? Is freeze detection even enabled?
            if (!Supervisor.getInstance().getFlags().isGameStartedYet() ||
                    !Supervisor.getConfig().getWatchdogSettings().isFreezeDetectionEnabled())
            {
                return;
            }

            // The client hasn't rendered something in 5 seconds. This usually indicates that the game has frozen.
            if (Instant.now().toEpochMilli() - LAST_RENDERED_TIME >= Supervisor.getConfig().getWatchdogSettings().getFreezeDetectionThreshold())
            {
                WNT.getLogger().error("--== Supervisor has detected a client-side freeze! ==--");
                Supervisor.getEventBus().post(new ClientFreezeEvent(Instant.now().toEpochMilli() - LAST_RENDERED_TIME));
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown()
    {
        freezeDetector.shutdownNow();
    }
}
