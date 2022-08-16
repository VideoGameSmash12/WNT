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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.event.module.ModuleDisabledEvent;
import me.videogamesm12.wnt.event.module.ModuleEnabledEvent;
import net.kyori.adventure.key.Key;

import java.io.*;
import java.util.*;

/**
 * <h1>ModuleManager</h1>
 * Manages WNT's module registration and loading.
 */
public class ModuleManager
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    //--
    private static final Map<Key, Module> modules = new HashMap<>();

    /**
     * Registers a module.
     * @param moduleClass   Class<? extends Module>
     */
    public static void register(Class<? extends Module> moduleClass)
    {
        try
        {
            Module instance = moduleClass.getDeclaredConstructor().newInstance();
            modules.put(instance.getKey(), instance);

            if (isEnabled(instance.getKey()))
                enable(instance);
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to register module " + moduleClass.getSimpleName());
            ex.printStackTrace();
        }
    }

    /**
     * Unregisters a module.
     * @param module   Module
     */
    public static <T extends Module> void unregister(T module)
    {
        if (!isRegistered(module.getKey()))
            return;

        try
        {
            // Disable the module before removing it
            if (isEnabled(module.getKey()))
            {
                module.stop();
                disable(module);
            }

            modules.remove(module.getKey());
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to unregister module " + module.getKey().asString());
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getModule(Key key)
    {
        if (!isRegistered(key))
        {
            throw new IllegalStateException("The module " + key.asString() + " is not currently registered");
        }

        return (T) modules.get(key);
    }

    public static boolean isRegistered(Key key)
    {
        return modules.containsKey(key);
    }

    public static boolean isEnabled(Key key)
    {
        return WNT.CONFIG.getEnabledModules().contains(key.asString());
    }

    public static <M extends Module> void enable(M module)
    {
        if (!isEnabled(module.getKey()))
            WNT.CONFIG.getEnabledModules().add(module.getKey().asString());

        if (!module.isStarted())
            module.start();

        ModuleEnabledEvent.EVENT.invoker().onEnable(module);
    }

    public static <M extends Module> void disable(M module)
    {
        if (isEnabled(module.getKey()) && module.isStarted())
            module.stop();

        WNT.CONFIG.getEnabledModules().remove(module.getKey().asString());
        ModuleDisabledEvent.EVENT.invoker().onDisable(module);
    }

    public static File getConfigFolder()
    {
        File folder = new File(WNT.getWNTFolder(), "modules");

        if (!folder.exists())
            folder.mkdir();

        return folder;
    }

    public static <T> T getModuleSettings(Key key, Class<T> settingsClass)
    {
        try
        {
            FileReader fileReader = new FileReader(new File(getConfigFolder(), key.namespace() + "." + key.value() + ".json"));
            return gson.fromJson(fileReader, settingsClass);
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to load module settings");
            ex.printStackTrace();
            return null;
        }
    }

    public static <T> void saveModuleSettings(Key key, T settings)
    {
        try (Writer writer = new FileWriter(key.namespace() + "." + key.value() + ".json"))
        {
            gson.toJson(settings, writer);
        }
        catch (Exception ex)
        {
            WNT.LOGGER.error("Failed to save module settings");
            ex.printStackTrace();
        }
    }
}
