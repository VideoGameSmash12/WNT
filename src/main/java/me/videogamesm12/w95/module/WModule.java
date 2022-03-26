package me.videogamesm12.w95.module;

import me.shedaniel.autoconfig.ConfigData;
import me.videogamesm12.w95.meta.ModuleInfo;

public interface WModule
{
    default void initialize()
    {
    }

    default void onStart()
    {
    }

    default void onStop()
    {
    }

    default String getName()
    {
        return getMeta().name();
    }

    default boolean isEnabled()
    {
        return getConfiguration().isEnabled();
    }

    default void disable()
    {
        onStop();
        getConfiguration().setEnabled(false);
    }

    default void enable()
    {
        onStart();
        getConfiguration().setEnabled(true);
    }

    default ModuleInfo getMeta()
    {
        return getClass().getAnnotation(ModuleInfo.class);
    }

    ModuleConfiguration getConfiguration();

    class ModuleConfiguration implements ConfigData
    {
        boolean enabled;

        public boolean isEnabled()
        {
            return enabled;
        }

        public void setEnabled(boolean value)
        {
            enabled = value;
        }
    }
}
