package me.videogamesm12.wnt.module;

import me.shedaniel.autoconfig.ConfigData;
import me.videogamesm12.wnt.meta.ModuleInfo;

import java.util.Map;

public interface WModule
{
    default void onInitialize()
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
        return true;
    }

    default void initialize()
    {
        onInitialize();
    }

    default void disable()
    {
        onStop();
    }

    default void enable()
    {
        onStart();
    }

    default ModuleInfo getMeta()
    {
        return getClass().getAnnotation(ModuleInfo.class);
    }
}
