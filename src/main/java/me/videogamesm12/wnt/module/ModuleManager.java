package me.videogamesm12.wnt.module;

import me.videogamesm12.wnt.WNT;

import java.util.*;

/**
 * <h1>ModuleManager</h1>
 * Manages WNT's module registration and loading.
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
            //--
            instance.initialize();
            //--
            if (instance.isEnabled())
                instance.enable();
            //--
            MODULES.put(moduleClass, instance);
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to register module " + moduleClass.getSimpleName());
            WNT.LOGGER.error(ex);
        }
    }

    /**
     * Unregisters a module.
     * @param moduleClass   Class<? extends WModule>
     */
    public <T extends WModule> void unregister(Class<T> moduleClass)
    {
        if (!MODULES.containsKey(moduleClass))
            return;

        try
        {
            T module = getModule(moduleClass);

            // Unregister the module before removing it
            if (module.isEnabled())
                module.disable();

            MODULES.remove(moduleClass);
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to unregister module " + moduleClass.getSimpleName());
            WNT.LOGGER.error(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends WModule> T getModule(Class<T> moduleClass)
    {
        return (T) MODULES.get(moduleClass);
    }
}