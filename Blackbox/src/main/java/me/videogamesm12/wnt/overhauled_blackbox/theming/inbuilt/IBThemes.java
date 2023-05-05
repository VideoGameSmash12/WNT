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

package me.videogamesm12.wnt.overhauled_blackbox.theming.inbuilt;

import com.formdev.flatlaf.util.SystemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.overhauled_blackbox.theming.ITheme;

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
