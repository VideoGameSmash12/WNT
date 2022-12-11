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

import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.events.ModuleRegisteredEvent;
import me.videogamesm12.wnt.events.ModuleUnregisteredEvent;

import java.io.File;
import java.util.*;

/**
 * <h1>ModuleManager</h1>
 * Manages WNT's module registration and loading.
 */
public class ModuleManager
{
    @Getter
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();

    public boolean isRegistered(Class<? extends Module> moduleClass)
    {
        return modules.containsKey(moduleClass);
    }

    /**
     * Registers and (if enabled) starts a module.
     * @param moduleClass   Class<? extends Module>
     */
    public <T extends Module> void register(Class<T> moduleClass)
    {
        try
        {
            Module instance = moduleClass.getDeclaredConstructor().newInstance();
            instance.start();
            //--
            if (WNT.getConfig().isEnabled(moduleClass))
                instance.enable();
            //--
            modules.put(moduleClass, instance);
            WNT.getEventBus().post(new ModuleRegisteredEvent<>(moduleClass));
        }
        catch (Throwable ex)
        {
            WNT.getLogger().error("Failed to register module " + moduleClass.getSimpleName(), ex);
        }
    }

    /**
     * Unregisters a module.
     * @param moduleClass   Class<? extends Module>
     */
    public <T extends Module> void unregister(Class<T> moduleClass)
    {
        if (!modules.containsKey(moduleClass))
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

            modules.remove(moduleClass);
            WNT.getEventBus().post(new ModuleUnregisteredEvent<>(moduleClass));
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to unregister module " + moduleClass.getSimpleName(), ex);
        }
    }

    public void stopAll()
    {
        modules.values().forEach(Module::onStop);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass)
    {
        if (!isRegistered(moduleClass))
        {
            throw new IllegalStateException("The module " + moduleClass.getSimpleName() + " is not currently registered");
        }

        return (T) modules.get(moduleClass);
    }

    public <T extends Module> T getModule(String name)
    {
        // OH MY GOD WHY MUST I DO THIS OH GOD IT IS HORRIBLE
        Map.Entry<Class<? extends Module>, Module> entry = modules.entrySet().stream().filter(set -> set.getKey().getSimpleName().equalsIgnoreCase(name)).findFirst().orElse(null);

        if (entry == null)
            return null;

        // OH THE PAIN IT IS UNBEARABLE WHY CAN'T I CHECK IF SHIT IS AN INSTANCE OF THE FUCKING T?
        return (T) entry.getValue();
    }

    public boolean isModuleRegistered(String name)
    {
        return modules.keySet().stream().anyMatch(clazz -> clazz.getSimpleName().equalsIgnoreCase(name));
    }

    public List<String> getModuleNames()
    {
        List<String> names = new ArrayList<>();

        modules.forEach((aClass, module) -> names.add(module.getMeta().name()));

        return modules.entrySet().stream().map((fuck) -> fuck.getValue().getMeta().name()).toList();
    }

    public static File getModulesFolder()
    {
        File folder = new File(WNT.getWNTFolder(), "modules");

        if (!folder.exists())
            folder.mkdirs();

        return folder;
    }
}
