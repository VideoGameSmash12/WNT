package me.videogamesm12.wnt.blackbox.theming.inbuilt;

import me.videogamesm12.wnt.blackbox.theming.ITheme;
import me.videogamesm12.wnt.blackbox.theming.IThemeProvider;
import me.videogamesm12.wnt.blackbox.theming.IThemeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IBThemeProvider implements IThemeProvider
{
    private static final Map<String, ITheme> themes = new HashMap<>();

    static
    {
        Arrays.stream(IBThemes.values()).forEach(theme -> themes.put(theme.getInternalName(), theme));
    }

    @Override
    public Map<String, ITheme> getThemes()
    {
        return themes;
    }

    @Override
    public List<IThemeType> getTypes()
    {
        return List.of(IBThemeType.values());
    }
}
