package me.videogamesm12.wnt.blackbox.theming.custom.flatlaf;

import com.formdev.flatlaf.FlatLaf;
import me.videogamesm12.wnt.blackbox.theming.ITheme;
import me.videogamesm12.wnt.blackbox.Blackbox;
import me.videogamesm12.wnt.blackbox.theming.IThemeProvider;
import me.videogamesm12.wnt.blackbox.theming.IThemeType;

import java.io.File;
import java.util.*;

public class CFThemeProvider implements IThemeProvider
{
    public static final IThemeType TYPE = new IThemeType()
    {
        @Override
        public String getLabel()
        {
            return "Custom FlatLAF Themes";
        }

        @Override
        public int getId()
        {
            return 69420;
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

        Arrays.stream(Objects.requireNonNull(getThemesFolder().listFiles())).filter(file -> file.getName().endsWith(".properties")).forEach(file ->
        {
            CFTheme theme = new CFTheme(file);
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
        File folder = new File(Blackbox.getFolder(), "themes/flatlaf");

        if (!folder.exists())
        {
            folder.mkdirs();
        }

        return folder;
    }
}
