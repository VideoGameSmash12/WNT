package me.videogamesm12.wnt.supervisor;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.supervisor.event.ClientFreezeDetected;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

import java.time.Instant;
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
    private SupervisorThread THREAD = null;

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