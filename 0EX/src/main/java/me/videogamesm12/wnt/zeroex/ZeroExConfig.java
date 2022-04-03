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
