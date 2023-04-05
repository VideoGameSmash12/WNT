package me.videogamesm12.wnt.toolbox.providers;

import me.videogamesm12.wnt.module.IModuleProvider;
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.toolbox.modules.DataQueryStorage;
import me.videogamesm12.wnt.toolbox.modules.LockupProtection;

import java.util.List;

public class ModuleProvider implements IModuleProvider
{
    @Override
    public List<Class<? extends Module>> getModuleClasses()
    {
        return List.of(DataQueryStorage.class, LockupProtection.class);
    }
}
