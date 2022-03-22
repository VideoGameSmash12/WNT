package me.videogamesm12.w95.module;

import me.shedaniel.autoconfig.ConfigData;
import me.videogamesm12.w95.meta.ModuleInfo;

public interface WModule
{
    default void initialize()
    {
    }

    default void start()
    {
    }

    default void stop()
    {
    }

    default String getName()
    {
        return getClass().isAnnotationPresent(ModuleInfo.class) ? getClass().getAnnotation(ModuleInfo.class).name() : getClass().getSimpleName();
    }

    default boolean isEnabled()
    {
        return getConfiguration().isEnabled();
    }

    default void setEnabled(boolean value)
    {
        setEnabled(value, true);
    }

    default void setEnabled(boolean value, boolean startStop)
    {
        getConfiguration().enabled = value;

        if (startStop)
        {
            if (value)
                start();
            else
                stop();
        }
    }

    default void toggle()
     {
         toggle(true);
     }

    default void toggle(boolean startStop)
    {
        setEnabled(!isEnabled(), startStop);
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
