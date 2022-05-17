package me.videogamesm12.wnt.blackbox.menus;

import me.videogamesm12.wnt.blackbox.Blackbox;

import javax.swing.*;

/**
 * <h1>SettingsMenu</h1>
 * The menu for all GUI-specific settings.
 */
public class SettingsMenu extends JMenu
{
    private final ThemeMenu themes = new ThemeMenu();
    private final JCheckBoxMenuItem autoRefresh = new JCheckBoxMenuItem("Auto-refresh", Blackbox.CONFIG.autoUpdate());
    private final JCheckBoxMenuItem showOnStartup = new JCheckBoxMenuItem("Show on startup", Blackbox.CONFIG.showOnStartup());

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

        add(themes);
        addSeparator();
        add(showOnStartup);
        add(autoRefresh);
    }

    /**
     * <h2>ThemeMenu</h2>
     * A menu for the theme selection in the Supervisor GUI.
     */
    public static class ThemeMenu extends JMenu
    {
        private ButtonGroup group = new ButtonGroup();

        public ThemeMenu()
        {
            super("Theme");

            // For every theme, build a radio button for it.
            for (Blackbox.GUITheme guiTheme : Blackbox.GUITheme.values())
            {
                JRadioButtonMenuItem themeItem = new JRadioButtonMenuItem();
                //--
                if (guiTheme == Blackbox.CONFIG.getTheme())
                {
                    themeItem.setSelected(true);
                }
                //--
                themeItem.addActionListener((event) -> {
                    Blackbox.CONFIG.setTheme(guiTheme);
                    JOptionPane.showMessageDialog(this, "The changes will take effect when you restart Minecraft.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                });
                themeItem.setText(guiTheme.getName());
                //--
                group.add(themeItem);
                add(themeItem);
            }
        }
    }
}