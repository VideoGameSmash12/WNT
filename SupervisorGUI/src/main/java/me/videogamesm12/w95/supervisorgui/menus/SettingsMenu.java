package me.videogamesm12.w95.supervisorgui.menus;

import me.videogamesm12.w95.supervisorgui.SupervisorGUI;

import javax.swing.*;

public class SettingsMenu extends JMenu
{
    private final ThemeMenu themes = new ThemeMenu();
    private final JCheckBoxMenuItem autoRefresh = new JCheckBoxMenuItem("Auto-refresh", SupervisorGUI.CONFIG.autoUpdate());
    private final JCheckBoxMenuItem showOnStartup = new JCheckBoxMenuItem("Show on startup", SupervisorGUI.CONFIG.showOnStartup());

    public SettingsMenu()
    {
        super("Settings");

        autoRefresh.addActionListener((event) -> {
            SupervisorGUI.CONFIG.setAutoUpdate(autoRefresh.isSelected());

            if (SupervisorGUI.CONFIG.autoUpdate())
                SupervisorGUI.GUI.scheduleRefresh();
            else
                SupervisorGUI.GUI.cancelRefresh();
        });
        showOnStartup.addActionListener((event) -> SupervisorGUI.CONFIG.setShowOnStartup(showOnStartup.isSelected()));

        add(themes);
        add(new JPopupMenu.Separator());
        add(showOnStartup);
        add(autoRefresh);
    }

    public static class ThemeMenu extends JMenu
    {
        private ButtonGroup group = new ButtonGroup();

        public ThemeMenu()
        {
            super("Theme");

            for (SupervisorGUI.GUITheme guiTheme : SupervisorGUI.GUITheme.values())
            {
                JRadioButtonMenuItem themeItem = new JRadioButtonMenuItem();
                //--
                if (guiTheme == SupervisorGUI.CONFIG.getTheme())
                {
                    themeItem.setSelected(true);
                }
                //--
                themeItem.addActionListener((event) -> {
                    SupervisorGUI.CONFIG.setTheme(guiTheme);
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