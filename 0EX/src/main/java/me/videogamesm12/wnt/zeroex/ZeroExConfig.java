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

package me.videogamesm12.wnt.zeroex;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "wnt-0ex")
public class ZeroExConfig implements ConfigData
{
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    private Attributes attributes = new Attributes();

    public Attributes getAttributes()
    {
        return attributes;
    }

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    private ZText text = new ZText();

    public ZText getText()
    {
        return text;
    }

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    private ZNetwork network = new ZNetwork();

    public ZNetwork getNetwork()
    {
        return network;
    }

    public static class ZNetwork
    {
        @Getter
        @Setter
        public boolean disableNbtCap = true;
    }

    public static class ZText
    {
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private final ClickEvents clickEvents = new ClickEvents();

        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        private final HoverEvents hoverEvents = new HoverEvents();

        public ClickEvents clickEvents()
        {
            return clickEvents;
        }

        public HoverEvents hoverEvents()
        {
            return hoverEvents;
        }

        public static class ClickEvents
        {
            @Getter
            @Setter
            private ZText.ClickEvents.RCPatchMethod runCommandFix = ZText.ClickEvents.RCPatchMethod.PROMPT;

            @Getter
            @Setter
            private boolean scfEnabled = true;

            // RC abbreviates to "runCommands"
            public enum RCPatchMethod
            {
                OFF,
                PROMPT,
                SILENT
            }
        }

        public static class HoverEvents
        {
            @Getter
            @Setter
            private ZText.HoverEvents.BEUPatchMethod beuFix = ZText.HoverEvents.BEUPatchMethod.VISIBLE;

            @Getter
            @Setter
            private boolean beiFixEnabled = true;

            @Getter
            @Setter
            private boolean biiFixEnabled = true;

            public enum BEUPatchMethod
            {
                OFF,
                SILENT,
                VISIBLE
            }
        }
    }

    public static class Attributes
    {
        @Getter
        @Setter
        private boolean nmsfEnabled = true;
    }
}
