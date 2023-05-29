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

package me.videogamesm12.wnt.cfx.config;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Config(name = "wnt-cfx")
public class CFXConfig implements ConfigData
{
    @ConfigEntry.Category("nbt")
    @ConfigEntry.Gui.TransitiveObject
    private NBT nbtPatches = new NBT();

    @ConfigEntry.Category("packet")
    @ConfigEntry.Gui.TransitiveObject
    private Packet packetPatches = new Packet();

    @ConfigEntry.Category("render")
    @ConfigEntry.Gui.TransitiveObject
    private Render rendererPatches = new Render();

    @ConfigEntry.Category("text")
    @ConfigEntry.Gui.TransitiveObject
    private TextComponents compPatches = new TextComponents();

    @Getter
    public static class Render
    {
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private EntityRender entities = new EntityRender();

        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private Hud hud = new Hud();

        @Getter
        public static class EntityRender
        {
            @ConfigEntry.Category("render")
            @ConfigEntry.Gui.Tooltip
            private boolean entityNameSizeLimitEnabled = true;

            @ConfigEntry.Category("render")
            @ConfigEntry.Gui.Tooltip
            private int entityNameSizeLimit = 255;
        }

        @Getter
        public static class Hud
        {
            @ConfigEntry.Category("render")
            @ConfigEntry.Gui.Tooltip
            private boolean heartCountLimitEnabled = true;

            @ConfigEntry.Category("render")
            @ConfigEntry.Gui.Tooltip
            private int maxHeartsToRender = 32;
        }
    }

    @Getter
    @Setter
    public static class NBT
    {
        @ConfigEntry.Category("nbt")
        @ConfigEntry.Gui.Tooltip
        private boolean sizeLimitEnabled = false;
    }

    @Getter
    @Setter
    public static class Packet
    {
        @ConfigEntry.Category("packet")
        @ConfigEntry.Gui.Tooltip
        private boolean particleLimitEnabled = true;

        @ConfigEntry.Category("packet")
        @ConfigEntry.Gui.Tooltip
        private int particleLimit = 500;
    }

    @Getter
    public static class TextComponents
    {
        @ConfigEntry.Category("text")
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private GText generalPatches = new GText();

        @ConfigEntry.Category("text")
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private TText translatePatches = new TText();

        @ConfigEntry.Category("text")
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private CText clickPatches = new CText();

        @Getter
        @Setter
        public static class CText
        {
            @ConfigEntry.Gui.Tooltip
            private RCMode rcMode = RCMode.PROMPT;

            @ConfigEntry.Gui.Tooltip
            private boolean suggestLimitEnabled = true;

            @ConfigEntry.Gui.Tooltip
            private int suggestLimit = 255;

            public enum RCMode
            {
                OFF,
                PROMPT,
                SILENT
            }
        }

        @Getter
        @Setter
        public static class TText
        {
            @ConfigEntry.Gui.Tooltip
            private boolean boundaryPatchEnabled = true;

            @ConfigEntry.Gui.Tooltip
            private boolean placeholderLimitEnabled = true;
        }

        @Getter
        @Setter
        public static class GText
        {
            @ConfigEntry.Gui.Tooltip
            private DAFMode doubleArrayFix = DAFMode.VANILLA;

            public enum DAFMode
            {
                OBVIOUS,    // Replaces payloads with text saying "*** Component array is empty ***"
                OFF,        // No intervention
                VANILLA     // Throws a JSON parse exception instead
            }
        }
    }
}
