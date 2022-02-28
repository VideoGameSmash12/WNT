package me.videogamesm12.w95.supervisor;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.w95.W95;
import me.videogamesm12.w95.meta.ModuleInfo;
import me.videogamesm12.w95.module.WModule;
import me.videogamesm12.w95.supervisor.event.ClientFreezeDetected;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class Supervisor implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        W95.MODULES.register(SupervisorModule.class);
    }

    @ModuleInfo(name = "Supervisor", description = "Powerhouse tool that offers control over your Minecraft client.")
    public static class SupervisorModule extends Thread implements WModule, ClientLifecycleEvents.ClientStopping
    {
        public static long LAST_RENDERED = Instant.now().toEpochMilli();
        public static SupervisorConfig CONFIG = null;
        //--
        private final Timer automation = new Timer();

        public SupervisorModule()
        {
            super("Supervisor");
            start();
        }

        @Override
        public void run()
        {
            ClientLifecycleEvents.CLIENT_STOPPING.register(this);
            //--
            AutoConfig.register(SupervisorConfig.class, GsonConfigSerializer::new);
            CONFIG = AutoConfig.getConfigHolder(SupervisorConfig.class).getConfig();
            //--
            // Detect client-side freezes
            if (CONFIG.detectFreezes())
            {
                automation.schedule(new FreezeDetector(), 0, 5000);
            }
            //--
            W95.LOGGER.info("Supervisor started");
        }

        @Override
        public void interrupt()
        {
            super.interrupt();
            automation.cancel();
        }

        @Override
        public void onClientStopping(MinecraftClient client)
        {
            AutoConfig.getConfigHolder(SupervisorConfig.class).save();
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

                    W95.LOGGER.error("--== Supervisor has detected a client-side freeze! ==--");
                }
            }
        }
    }

    @Config(name = "w95-supervisor")
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