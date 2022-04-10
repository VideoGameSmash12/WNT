package me.videogamesm12.wnt.example;

import me.shedaniel.autoconfig.annotation.Config;
import me.videogamesm12.wnt.module.Module;

public class ModuleTest extends Module
{
    public ModuleTest()
    {
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void onStop()
    {

    }

    @Override
    public Class<ModuleConfig> getConfigClass()
    {
        return ModuleConfig.class;
    }

    @Config(name = "wnt-module-test")
    public static class ModuleConfig extends MConfig
    {

    }
}
