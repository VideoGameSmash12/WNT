/*
 * Copyright (c) 2022 Video
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

package me.videogamesm12.wnt.blackbox.menus;

import me.videogamesm12.wnt.blackbox.Blackbox;

import javax.swing.*;
import java.util.Arrays;

/**
 * <h1>SettingsMenu</h1>
 * The menu for all GUI-specific settings.
 */
public class SettingsMenu extends JMenu
{
    private final JCheckBoxMenuItem autoRefresh = new JCheckBoxMenuItem("Auto-refresh", Blackbox.CONFIG.autoUpdate());
    private final JCheckBoxMenuItem showOnStartup = new JCheckBoxMenuItem("Show on startup", Blackbox.CONFIG.showOnStartup());
    private final JCheckBoxMenuItem ignoreFreezesDuringStartup = new JCheckBoxMenuItem("Ignore client freezes during startup", Blackbox.CONFIG.ignoreFreezesDuringStartup());

    public SettingsMenu()
    {
        super("Settings");

        autoRefresh.addActionListener((event) -> {
            Blackbox.CONFIG.setAutoUpdate(autoRefresh.isSelected());

            if (Blackbox.CONFIG.autoUpdate())
                Blackbox.GUI.scheduleRefresh();
            else
                Blackbox.GUI.cancelRefresh();
        });
        showOnStartup.addActionListener((event) -> Blackbox.CONFIG.setShowOnStartup(showOnStartup.isSelected()));
        ignoreFreezesDuringStartup.addActionListener((event) -> Blackbox.CONFIG.setIgnoreFreezesDuringStartup(ignoreFreezesDuringStartup.isSelected()));

        ThemeMenu themes = new ThemeMenu();
        add(themes);
        addSeparator();
        add(showOnStartup);
        add(ignoreFreezesDuringStartup);
        add(autoRefresh);
    }
    
    /**
     * <h2>ThemeMenu</h2>
     * A menu for the theme selection in the Blackbox.
     */
    public static class ThemeMenu extends JMenu
    {
        private final ButtonGroup group = new ButtonGroup();

        public ThemeMenu()
        {
            super("Theme");

            Arrays.stream(Blackbox.GUIThemeType.values()).forEach(type ->
            {
                JMenuItem label = new JMenuItem("--== " + type.getLabel() + " ==--");
                label.setEnabled(false);
                add(label);

                // For every theme, build a radio button for it.
                Arrays.stream(Blackbox.GUITheme.values()).filter(theme -> theme.getThemeType().equals(type) && theme.shouldShow()).forEach(theme ->
                {
                    JRadioButtonMenuItem themeItem = new JRadioButtonMenuItem();
                    //--
                    if (theme == Blackbox.CONFIG.getTheme())
                    {
                        themeItem.setSelected(true);
                    }
                    //--
                    themeItem.addActionListener((event) -> {
                        Blackbox.CONFIG.setTheme(theme);
                        theme.showOptionalChangeMessage();
                        JOptionPane.showMessageDialog(this, "The changes will take effect when you restart Minecraft.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    });
                    themeItem.setText(theme.getName());
                    themeItem.setToolTipText(theme.getDescription());
                    //--
                    group.add(themeItem);
                    add(themeItem);
                });
            });
        }
    }
}