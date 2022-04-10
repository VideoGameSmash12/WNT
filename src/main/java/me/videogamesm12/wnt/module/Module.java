package me.videogamesm12.wnt.module;

import lombok.Data;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public abstract class Module
{
    public MConfig config;

    public Module()
    {
        AutoConfig.register(getConfigClass(), GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(getConfigClass()).getConfig();
    }

    public final void enable()
    {
        getConfig().setEnabled(true);
        onEnable();
    }

    public final void disable()
    {
        getConfig().setEnabled(false);
        onDisable();
    }

    public final void start()
    {
        onStart();
    }

    public final void stop()
    {
        onStop();
    }

    public void onDisable()
    {
    }

    public void onEnable()
    {
    }

    public abstract void onStart();

    public abstract void onStop();

    public final <T extends MConfig> T getConfig()
    {
        return (T) config;
    }

    public abstract Class<? extends MConfig> getConfigClass();

    public final boolean isEnabled()
    {
        return getConfig().isEnabled();
    }

    @Data
    public static class MConfig implements ConfigData
    {
        private boolean enabled;
    }
}