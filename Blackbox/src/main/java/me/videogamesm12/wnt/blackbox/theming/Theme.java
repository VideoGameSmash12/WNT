package me.videogamesm12.wnt.blackbox.theming;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.Blackbox;
import org.netbeans.swing.laf.dark.DarkMetalLookAndFeel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.io.File;
import java.io.FileInputStream;

/**
 * <h1>GUITheme</h1>
 * The method the GUI uses to get the selected theme.
 * --
 * TODO: Replace this with something more... modular.
 */
public enum Theme
{
    ARC_DARK("Arc Dark", ThemeType.FLATLAF, FlatArcDarkIJTheme.class, true),
    ARC_DARK_HC("Arc Dark Contrast", "A variant of Arc Dark with better text box contrast.", ThemeType.FLATLAF, FlatArcDarkContrastIJTheme.class, true),
    CARBON("Carbon", ThemeType.FLATLAF, FlatCarbonIJTheme.class, true),
    COBALT_2("Cobalt 2", ThemeType.FLATLAF, FlatCobalt2IJTheme.class, true),
    CUSTOM("Custom", "Loads a theme from .minecraft/wnt/blackbox/theme.json.", ThemeType.FLATLAF, FlatMaterialDarkerIJTheme.class, true),
    DARK("Material Darker", ThemeType.FLATLAF, FlatMaterialDarkerIJTheme.class, true),
    DARK_HC("Material Darker Contrast", "A variant of Material Darker with better text box contrast.", ThemeType.FLATLAF, FlatMaterialDarkerContrastIJTheme.class, true),
    LIGHT("Material Lighter", ThemeType.FLATLAF, FlatMaterialLighterIJTheme.class, true),
    LIGHT_HC("Material Lighter Contrast", "A variant of Material Lighter with better text box contrast.", ThemeType.FLATLAF, FlatMaterialLighterContrastIJTheme.class, true),
    DEEP_OCEAN("Material Deep Ocean", ThemeType.FLATLAF, FlatMaterialDeepOceanIJTheme.class, true),
    DEEP_OCEAN_HC("Material Deep Ocean Contrast", "A variant of Material Deep Ocean with better text box contrast.", ThemeType.FLATLAF, FlatMaterialDeepOceanContrastIJTheme.class, true),
    NORD("Nord", ThemeType.FLATLAF, FlatNordIJTheme.class, true),
    ONE_DARK("One Dark", ThemeType.FLATLAF, FlatOneDarkIJTheme.class, true),
    PURPLE("Dark Purple", ThemeType.FLATLAF, FlatDarkPurpleIJTheme.class, true),
    //--
    NETBEANS_DARK_METAL("Dark Metal", ThemeType.NETBEANS, DarkMetalLookAndFeel.class, true),
    //--
    METAL("Metal", ThemeType.BUILT_IN, MetalLookAndFeel.class, true),
    MOTIF("Motif", "A hilariously outdated theme that hasn't changed at all since the 1990s.", ThemeType.BUILT_IN, "com.sun.java.swing.plaf.motif.MotifLookAndFeel", true),
    SYSTEM("System", "A theme that automatically adapts to whatever operating system you are currently using.", ThemeType.BUILT_IN, UIManager.getSystemLookAndFeelClassName(), true),
    WINDOWS("Windows", "Ah yes, good ol' Win32.", ThemeType.BUILT_IN, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", SystemInfo.isWindows),
    WINDOWS_CLASSIC("Windows Classic", "Perfect for those who prefer function over form.", ThemeType.BUILT_IN, "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel", SystemInfo.isWindows);

    private String themeName = null;
    private String themeDescription = null;
    private Class<? extends BasicLookAndFeel> themeClass = null;
    private ThemeType type = null;
    private String internalPackage = null;
    private boolean shouldShow = false;

    Theme(String themeName, String themeDescription, ThemeType type, Class<? extends BasicLookAndFeel> themeClass, boolean shouldShow)
    {
        this.themeName = themeName;
        this.themeDescription = themeDescription;
        this.type = type;
        this.themeClass = themeClass;
        this.shouldShow = shouldShow;
    }

    Theme(String themeName, String themeDescription, ThemeType type, String internalPackage, boolean shouldShow)
    {
        this.themeName = themeName;
        this.themeDescription = themeDescription;
        this.type = type;
        this.internalPackage = internalPackage;
        this.shouldShow = shouldShow;
    }

    Theme(String themeName, ThemeType type, Class<? extends BasicLookAndFeel> themeClass, boolean shouldShow)
    {
        this.themeName = themeName;
        this.type = type;
        this.themeClass = themeClass;
        this.shouldShow = shouldShow;
    }

    Theme(String themeName, ThemeType type, String internalPackage, boolean shouldShow)
    {
        this.themeName = themeName;
        this.type = type;
        this.internalPackage = internalPackage;
        this.shouldShow = shouldShow;
    }

    public String getName()
    {
        return themeName;
    }

    public String getDescription()
    {
        return themeDescription;
    }

    public ThemeType getThemeType()
    {
        return type;
    }

    public boolean shouldShow()
    {
        return shouldShow;
    }


    public void showOptionalChangeMessage()
    {
        if (this == CUSTOM)
        {
            JOptionPane.showMessageDialog(null, "Just so you know, the custom theme will need to be located at .minecraft/wnt/blackbox/theme.json with this theme enabled.", "Notice", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void apply()
    {
        try
        {
            // We are using a custom theme for FlatLAF.
            if (this == CUSTOM)
            {
                File file = new File(Blackbox.getBlackboxFolder(), "theme.json");
                if (!file.exists())
                {
                    // Fallback
                    FlatMaterialDarkerIJTheme.setup();
                    WNT.getLogger().warn("You set your theme for the Blackbox to Custom, but no theme was found at .minecraft/wnt/blackbox/theme.json. Please put a theme JSON file there.");
                }
                else
                {
                    IntelliJTheme.setup(new FileInputStream(file));
                }
            }
            // We are using something that belongs to FlatLAF.
            else if (themeClass != null)
            {
                if (IntelliJTheme.ThemeLaf.class.isAssignableFrom(themeClass))
                {
                    themeClass.getMethod("setup").invoke(this);
                }
                else
                {
                    UIManager.setLookAndFeel(themeClass.getDeclaredConstructor().newInstance());
                }
            }
            // We are using something that isn't FlatLAF.
            else if (internalPackage != null)
            {
                UIManager.setLookAndFeel(internalPackage);
            }
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to apply currently active theme", ex);
        }
    }
}
