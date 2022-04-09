package me.videogamesm12.wnt.zeroex;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixins;

public class ZeroEx implements ModInitializer
{
    public static ZeroExConfig CONFIG = null;

    @Override
    public void onInitialize()
    {
        AutoConfig.register(ZeroExConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ZeroExConfig.class).getConfig();
    }
}
