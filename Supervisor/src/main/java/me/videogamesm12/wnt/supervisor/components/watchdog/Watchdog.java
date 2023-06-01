/*
 * Copyright (c) 2023 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.videogamesm12.wnt.supervisor.components.watchdog;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.api.SVComponent;
import me.videogamesm12.wnt.supervisor.api.event.ClientFreezeEvent;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Watchdog extends Thread implements SVComponent
{
    public static long LAST_RENDERED_TIME = 0L;
    //--
    private final ScheduledExecutorService freezeDetector = new ScheduledThreadPoolExecutor(1);

    @Override
    public String identifier()
    {
        return "wnt:watchdog";
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
