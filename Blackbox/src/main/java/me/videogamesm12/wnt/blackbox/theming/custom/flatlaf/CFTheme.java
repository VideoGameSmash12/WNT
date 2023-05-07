package me.videogamesm12.wnt.blackbox.theming.custom.flatlaf;

import com.formdev.flatlaf.FlatPropertiesLaf;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.theming.ITheme;
import me.videogamesm12.wnt.blackbox.theming.IThemeType;

import java.io.File;

public class CFTheme implements ITheme
{
    private final File file;
    private FlatPropertiesLaf laf;

    public CFTheme(File file)
    {
        this.file = file;
    }

    @Override
    public String getInternalName()
    {
        return "bbCusFlatLAF:" + file.getName();
    }

    @Override
    public String getThemeName()
    {
        return file.getName();
    }

    @Override
    public String getThemeDescription()
    {
        return null;
    }

    @Override
    public IThemeType getType()
    {
        return CFThemeProvider.TYPE;
    }

    @Override
    public String getThemeClass()
    {
        return null;
    }

    @Override
    public boolean isSupposedToShow()
    {
        return true;
    }

    @Override
    public void apply()
    {
        if (laf == null)
        {
            try
            {
                laf = new FlatPropertiesLaf(file.getName(), file);
            }
            catch (Exception ex)
            {
                WNT.getLogger().error("Failed to load custom FlatLAF theme", ex);
            }
        }

        FlatPropertiesLaf.setup(laf);
    }

    @Override
    public void showOptionalMessage()
    {
    }
}
