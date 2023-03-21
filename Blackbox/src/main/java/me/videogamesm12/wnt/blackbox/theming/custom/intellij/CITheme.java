package me.videogamesm12.wnt.blackbox.theming.custom.intellij;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.theming.ITheme;
import me.videogamesm12.wnt.blackbox.theming.IThemeType;

import java.io.*;

public class CITheme implements ITheme
{
    private final File file;

    public CITheme(File file)
    {
        this.file = file;
    }

    @Override
    public String getInternalName()
    {
        return "bbCusIntellij:" + file.getName();
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
        return CIThemeProvider.TYPE;
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
        try
        {
            IntelliJTheme.setup(new FileInputStream(file));
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("WTF?", ex);
            FlatMaterialDarkerIJTheme.setup();
        }
    }

    @Override
    public void showOptionalMessage()
    {
    }
}
