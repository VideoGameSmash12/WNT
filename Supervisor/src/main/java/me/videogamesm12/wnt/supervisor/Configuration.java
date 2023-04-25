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

package me.videogamesm12.wnt.supervisor;

import lombok.Getter;
import lombok.Setter;
import me.videogamesm12.wnt.supervisor.components.fantasia.ConnectionType;

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

        private ConnectionType connectionType = ConnectionType.TELNET;
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
