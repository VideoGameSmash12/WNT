package me.videogamesm12.w95.module;

import me.videogamesm12.w95.W95;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>ModuleManager</h1>
 * Manages W95's module registration and loading.
 */
public class ModuleManager
{
    private final Map<Class<? extends WModule>, WModule> MODULES = new HashMap<>();

    public boolean isRegistered(Class<? extends WModule> moduleClass)
    {
        return MODULES.containsKey(moduleClass);
    }

    /**
     * Registers a module.
     * @param moduleClass   Class<? extends WModule>
     */
    public void register(Class<? extends WModule> moduleClass)
    {
        try
        {
            WModule instance = moduleClass.getDeclaredConstructor().newInstance();
            instance.initialize();
            MODULES.put(moduleClass, instance);
        }
        catch (Exception ex)
        {
            W95.LOGGER.error("Failed to register module " + moduleClass.getSimpleName());
            W95.LOGGER.error(ex);
        }
    }

    public Collection<WModule> getModules()
    {
        return MODULES.values();
    }

    public WModule getModule(Class<? extends WModule> moduleClass)
    {
        return MODULES.get(moduleClass);
    }
}
