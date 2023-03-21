package me.videogamesm12.wnt.blackbox.theming;

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
            WNT.getLogger().info("Loaded " + types.size() + " theme types from mod " + container.getProvider().getOrigin().getParentModId());
            //--
            Map<String, ITheme> modThemes = themeProvider.getThemes();
            themes.putAll(modThemes);
            themeCount.addAndGet(modThemes.size());
            WNT.getLogger().info("Loaded " + modThemes.size() + " themes from mod " + container.getProvider().getOrigin().getParentModId());
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
