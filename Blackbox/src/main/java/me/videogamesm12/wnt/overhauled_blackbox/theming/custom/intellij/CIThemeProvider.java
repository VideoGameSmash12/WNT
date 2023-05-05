package me.videogamesm12.wnt.overhauled_blackbox.theming.custom.intellij;

import com.formdev.flatlaf.FlatLaf;
import me.videogamesm12.wnt.overhauled_blackbox.Blackbox;
import me.videogamesm12.wnt.overhauled_blackbox.theming.ITheme;
import me.videogamesm12.wnt.overhauled_blackbox.theming.IThemeProvider;
import me.videogamesm12.wnt.overhauled_blackbox.theming.IThemeType;

import java.io.File;
import java.util.*;

public class CIThemeProvider implements IThemeProvider
{
    public static final IThemeType TYPE = new IThemeType()
    {
        @Override
        public String getLabel()
        {
            return "Custom Intellij Themes";
        }

        @Override
        public int getId()
        {
            // Yeah, I had to.
            return 42069;
        }

        @Override
        public void update()
        {
            FlatLaf.updateUI();
        }
    };

    @Override
    public Map<String, ITheme> getThemes()
    {
        Map<String, ITheme> themeMap = new HashMap<>();

        Arrays.stream(Objects.requireNonNull(getThemesFolder().listFiles())).filter(file -> file.getName().endsWith(".theme.json")).forEach(file ->
        {
            CITheme theme = new CITheme(file);
            themeMap.put(theme.getThemeName(), theme);
        });

        return themeMap;
    }

    @Override
    public List<IThemeType> getTypes()
    {
        return Collections.singletonList(TYPE);
    }

    public static File getThemesFolder()
    {
        File folder = new File(Blackbox.getFolder(), "themes/intellij");

        if (!folder.exists())
        {
            folder.mkdirs();
        }

        return folder;
    }
}