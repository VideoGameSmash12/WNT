package me.videogamesm12.w95.zeroex;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class ZeroEx implements ModInitializer
{
    public static ZeroExConfig CONFIG = null;

    @Override
    public void onInitialize()
    {
        AutoConfig.register(ZeroExConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ZeroExConfig.class).getConfig();
    }

    @Config(name = "w95-0ex")
    public static class ZeroExConfig implements ConfigData
    {
        @ConfigEntry.Gui.Tooltip(count = 4)
        private HoverUUIDPatchMethod hoverUUIDPatchMethod = HoverUUIDPatchMethod.VISIBLE;

        public HoverUUIDPatchMethod getHoverUUIDPatchMethod()
        {
            return hoverUUIDPatchMethod;
        }

        public void setHoverUUIDPatchMethod(HoverUUIDPatchMethod method)
        {
            hoverUUIDPatchMethod = method;
        }
    }

    public enum HoverUUIDPatchMethod
    {
        OFF,
        SILENT,
        VISIBLE;
    }
}
