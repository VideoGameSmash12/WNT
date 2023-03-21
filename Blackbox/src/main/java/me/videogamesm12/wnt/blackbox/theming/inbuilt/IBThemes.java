package me.videogamesm12.wnt.blackbox.theming.inbuilt;

import com.formdev.flatlaf.util.SystemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.theming.ITheme;

import javax.swing.*;

@AllArgsConstructor
@Getter
public enum IBThemes implements ITheme
{
    DARK_METAL("Dark Metal", "A dark metallic theme for those feeling a bit edgy.", IBThemeType.NETBEANS, "org.netbeans.swing.laf.dark.DarkMetalLookAndFeel", true),
    METAL("Metal", "A nice-looking cross-platform theme with a tint of metal thrown in.", IBThemeType.BUILT_IN, "javax.swing.plaf.metal.MetalLookAndFeel", true),
    MOTIF("Motif", "A hilariously outdated theme that hasn't changed at all since the 1990s.", IBThemeType.BUILT_IN, "com.sun.java.swing.plaf.motif.MotifLookAndFeel", true),
    SYSTEM("System", "A theme that automatically adapts to whatever operating system you are currently using.", IBThemeType.BUILT_IN, UIManager.getSystemLookAndFeelClassName(), true),
    WINDOWS("Windows", "Ah yes, good ol' Win32.", IBThemeType.BUILT_IN, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", SystemInfo.isWindows),
    WINDOWS_CLASSIC("Windows Classic", "Perfect for those who prefer function over form.", IBThemeType.BUILT_IN, "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel", SystemInfo.isWindows);

    private final String themeName;
    private final String themeDescription;
    private final IBThemeType type;
    private final String themeClass;
    private final boolean supposedToShow;

    @Override
    public String getInternalName()
    {
        return name();
    }

    @Override
    public void apply()
    {
        try
        {
            UIManager.setLookAndFeel(themeClass);
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to apply built-in theme", ex);
        }
    }

    @Override
    public void showOptionalMessage()
    {
        // Do nothing.
    }
}
