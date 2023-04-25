package me.videogamesm12.wnt.supervisor;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Configuration
{
    private Fantasia fantasiaSettings = new Fantasia();

    private Network networkSettings = new Network();

    private Rendering renderingSettings = new Rendering();

    private Watchdog watchdogSettings = new Watchdog();

    @Getter
    @Setter
    public static class Fantasia
    {
        private int port = 6969;
    }

    @Getter
    @Setter
    public static class Network
    {
        private boolean ignoringEntitySpawns;

        private boolean ignoringExplosions;

        private boolean ignoringLightUpdates;

        private boolean ignoringParticleSpawns;
    }

    @Getter
    @Setter
    public static class Rendering
    {
        private boolean entityRenderingDisabled;

        private boolean gameRenderingDisabled;

        private boolean tileEntityRenderingDisabled;

        private boolean weatherRenderingDisabled;

        private boolean worldRenderingDisabled;
    }

    @Getter
    @Setter
    public static class Watchdog
    {
        private boolean freezeDetectionEnabled = true;

        private long freezeDetectionThreshold = 5000;
    }
}
