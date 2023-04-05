package me.videogamesm12.wnt.module;

import java.util.List;

public interface IModuleProvider
{
    List<Class<? extends Module>> getModuleClasses();
}
