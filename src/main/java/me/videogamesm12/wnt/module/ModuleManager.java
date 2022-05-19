/*
 * Copyright (c) 2022 Video
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
            MODULES.put(moduleClass, instance);
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to register module " + moduleClass.getSimpleName());
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass)
    {
        if (!isRegistered(moduleClass))
        {
            throw new IllegalStateException("The module " + moduleClass.getSimpleName() + " is not currently registered");
        }

        return (T) MODULES.get(moduleClass);
    }
}
