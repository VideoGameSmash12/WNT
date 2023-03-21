package me.videogamesm12.wnt.blackbox.theming;

import java.util.List;
import java.util.Map;

public interface IThemeProvider
{
    Map<String, ITheme> getThemes();

    List<IThemeType> getTypes();
}
