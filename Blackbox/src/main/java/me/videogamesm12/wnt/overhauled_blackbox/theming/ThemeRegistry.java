/*
 * Copyright (c) 2023 Video
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

package me.videogamesm12.wnt.overhauled_blackbox.theming;

import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ThemeRegistry
{
    @Getter
    private static final Map<String, ITheme> themes = new HashMap<>();

    @Getter
    private static final List<IThemeType> themeTypes = new ArrayList<>();

    public static void setupThemes()
    {
        AtomicInteger themeCount = new AtomicInteger(0);
        AtomicInteger themeTypeCount = new AtomicInteger(0);
        FabricLoader.getInstance().getEntrypointContainers("wnt-blackbox", IThemeProvider.class).forEach(container ->
        {
            IThemeProvider themeProvider = container.getEntrypoint();
            //--
            List<IThemeType> types = themeProvider.getTypes();
            themeTypes.addAll(types);
            themeTypeCount.addAndGet(types.size());
            WNT.getLogger().info("Loaded " + types.size() + " theme types from mod " + container.getProvider().getMetadata().getName());
            //--
            Map<String, ITheme> modThemes = themeProvider.getThemes();
            themes.putAll(modThemes);
            themeCount.addAndGet(modThemes.size());
            WNT.getLogger().info("Loaded " + modThemes.size() + " themes from mod " + container.getProvider().getMetadata().getName());
            //--

        });
        WNT.getLogger().info("Loaded " + themeCount.get() + " themes and " + themeTypeCount.get() + " theme types");
    }

    public static ITheme getTheme(String id)
    {
        if (themes.containsKey(id))
        {
            return themes.get(id);
        }
        else
        {
            throw new IllegalArgumentException("Theme not found: " + id);
        }
    }
}
