package me.videogamesm12.w95.module;

import me.videogamesm12.w95.W95;

import java.util.*;

/**
 * <h1>ModuleManager</h1>
 * Manages W95's module registration and loading.
 */
public class ModuleManager
{
    private final Map<Class<? extends WModule>, WModule> MODULES = new HashMap<>(); // Path

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

    /**
     * Unregisters a module.
     * @param moduleClass   Class<? extends WModule>
     */
    @SuppressWarnings("unchecked")
    public <T extends WModule> void unregister(Class<T> moduleClass)
    {
        if (!MODULES.containsKey(moduleClass))
            return;

        try
        {
            T module = (T) MODULES.get(moduleClass);

            // Unregister the module before removing it
            if (module.isEnabled())
                module.disable();

            MODULES.remove(moduleClass);
        }
        catch (Exception ex)
        {
            W95.LOGGER.error("Failed to unregister module " + moduleClass.getSimpleName());
            W95.LOGGER.error(ex);
        }
    }

    public WModule getModule(Class<? extends WModule> moduleClass)
    {
        return MODULES.get(moduleClass);
    }
}
