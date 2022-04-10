package me.videogamesm12.wnt.module;

import me.videogamesm12.wnt.WNT;

import java.util.*;

/**
 * <h1>ModuleManager</h1>
 * Manages WNT's module registration and loading.
 */
public class ModuleManager
{
    private final Map<Class<? extends Module>, Module> MODULES = new HashMap<>();

    public boolean isRegistered(Class<? extends Module> moduleClass)
    {
        return MODULES.containsKey(moduleClass);
    }

    /**
     * Registers a module.
     * @param moduleClass   Class<? extends Module>
     */
    public void register(Class<? extends Module> moduleClass)
    {
        try
        {
            Module instance = moduleClass.getDeclaredConstructor().newInstance();
            //--
            if (instance.isEnabled())
                instance.start();
            //--
            MODULES.put(instance.getClass(), instance);
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to register module " + moduleClass.getSimpleName());
            WNT.LOGGER.error(ex);
        }
    }

    /**
     * Unregisters a module.
     * @param moduleClass   Class<? extends Module>
     */
    public <T extends Module> void unregister(Class<T> moduleClass)
    {
        if (!MODULES.containsKey(moduleClass))
            return;

        try
        {
            T module = getModule(moduleClass);

            // Unregister the module before removing it
            if (module.isEnabled())
            {
                module.disable();
                module.stop();
            }

            MODULES.remove(moduleClass);
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to unregister module " + moduleClass.getSimpleName());
            WNT.LOGGER.error(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass)
    {
        return (T) MODULES.get(moduleClass);
    }
}
