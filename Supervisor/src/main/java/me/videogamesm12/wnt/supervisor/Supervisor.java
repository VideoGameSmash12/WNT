/*
 * Copyright (c) 2022 Video
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

package me.videogamesm12.wnt.supervisor;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.supervisor.event.ClientFreezeDetected;
import me.videogamesm12.wnt.supervisor.mixin.gui.DebugHudMixin;
import me.videogamesm12.wnt.supervisor.mixin.gui.InGameHudMixin;
import me.videogamesm12.wnt.supervisor.util.Fallbacks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <h1>Supervisor</h1>
 * A major component in WNT, offering better control over the client
 */
public class Supervisor implements ClientLifecycleEvents.ClientStopping, ModInitializer
{
    public static SupervisorConfig CONFIG = null;
    //--
    private static SupervisorThread THREAD = null;

    @Override
    public void onInitialize()
    {
        AutoConfig.register(SupervisorConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(SupervisorConfig.class).getConfig();
        //--
        ClientLifecycleEvents.CLIENT_STOPPING.register(this);
        //--
        THREAD = new SupervisorThread();
    }

    @Override
    public void onClientStopping(MinecraftClient client)
    {
        AutoConfig.getConfigHolder(SupervisorConfig.class).save();
    }

    public static List<String> getF3Info()
    {
        return THREAD != null ? THREAD.getF3Info() : new ArrayList<>();
    }

    public static class SupervisorThread extends Thread
    {
        public static long LAST_RENDERED = Instant.now().toEpochMilli();
        //--
        private final Timer automation = new Timer();

        public SupervisorThread()
        {
            super("Supervisor");
            start();
        }

        @Override
        public void run()
        {
            // Detect client-side freezes
            if (CONFIG.detectFreezes())
            {
                automation.schedule(new FreezeDetector(), 0, 5000);
            }
            //--
            WNT.LOGGER.info("Supervisor started");
        }

        @Override
        public void interrupt()
        {
            super.interrupt();
            automation.cancel();
        }

        public List<String> getF3Info()
        {
            synchronized (this)
            {
                try
                {
                    return ((DebugHudMixin) ((InGameHudMixin) MinecraftClient.getInstance().inGameHud).getDebugHud()).getLeftText();
                }
                catch (Exception | Error ex)
                {
                    return Fallbacks.getLeftText();
                }
            }
        }

        /**
         * <h3>FreezeDetector</h3>
         * <p>Supervisor's freeze detection works by injecting some code at the tail-end of the game's rendering method to
         *  store a timestamp for when the last time a frame successfully rendered occurs, then periodically checking
         *  through another thread if it exceeds 5 seconds.</p>
         * <p>This code is what checks the timestamps.</p>
         */
        public static class FreezeDetector extends TimerTask
        {
            @Override
            public void run()
            {
                // The client hasn't rendered something in 5 seconds. This usually indicates that the game has frozen.
                if (Instant.now().toEpochMilli() - LAST_RENDERED >= 5000)
                {
                    if (!ClientFreezeDetected.EVENT.invoker().onClientFreeze(LAST_RENDERED).isAccepted())
                    {
                        return;
                    }

                    WNT.LOGGER.error("--== Supervisor has detected a client-side freeze! ==--");
                }
            }
        }
    }

    @Config(name = "wnt-supervisor")
    public static class SupervisorConfig implements ConfigData
    {
        private boolean detectFreezes = true;

        private Network network = new Network();

        private Rendering rendering = new Rendering();

        public static class Network
        {
            private boolean ignoreEntitySpawns;

            private boolean ignoreExplosions;

            private boolean ignoreLightUpdates;

            private boolean ignoreParticleSpawns;

            public boolean ignoreEntitySpawns()
            {
                return ignoreEntitySpawns;
            }

            public boolean ignoreExplosions()
            {
                return ignoreExplosions;
            }

            public boolean ignoreLightUpdates()
            {
                return ignoreLightUpdates;
            }

            public boolean ignoreParticleSpawns()
            {
                return ignoreParticleSpawns;
            }

            public void setIgnoreEntitySpawns(boolean bool)
            {
                this.ignoreEntitySpawns = bool;
            }

            public void setIgnoreExplosionSpawns(boolean bool)
            {
                this.ignoreExplosions = bool;
            }

            public void setIgnoreLightUpdates(boolean bool)
            {
                this.ignoreLightUpdates = bool;
            }

            public void setIgnoreParticleSpawns(boolean bool)
            {
                this.ignoreParticleSpawns = bool;
            }
        }

        public static class Rendering
        {
            private boolean disableEntityRendering = false;

            private boolean disableGameRendering = false;

            private boolean disableTileEntityRendering = false;

            private boolean disableWeatherRendering = false;

            private boolean disableWorldRendering = false;

            public boolean disableEntityRendering()
            {
                return disableEntityRendering;
            }

            public boolean disableGameRendering()
            {
                return disableGameRendering;
            }

            public boolean disableTileEntityRendering()
            {
                return disableTileEntityRendering;
            }

            public boolean disableWeatherRendering()
            {
                return disableWeatherRendering;
            }

            public boolean disableWorldRendering()
            {
                return disableWorldRendering;
            }

            public void setDisableEntityRendering(boolean value)
            {
                this.disableEntityRendering = value;
            }

            public void setDisableGameRendering(boolean value)
            {
                this.disableGameRendering = value;
            }

            public void setDisableTileEntityRendering(boolean value)
            {
                this.disableTileEntityRendering = value;
            }

            public void setDisableWeatherRendering(boolean value)
            {
                this.disableWeatherRendering = value;
            }

            public void setDisableWorldRendering(boolean value)
            {
                this.disableWorldRendering = value;
            }
        }

        public boolean detectFreezes()
        {
            return detectFreezes;
        }

        public void setDetectFreezes(boolean value)
        {
            this.detectFreezes = value;
        }

        public Rendering rendering()
        {
            return rendering;
        }

        public Network network()
        {
            return network;
        }
    }
}