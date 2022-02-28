package me.videogamesm12.w95.module;

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

    default boolean isEnabled()
    {
        return true;
    }
}
