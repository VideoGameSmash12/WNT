package me.videogamesm12.wnt.blackbox.menus;

import me.videogamesm12.wnt.blackbox.SupervisorGUI;
import me.videogamesm12.wnt.supervisor.networking.PacketType;

import javax.swing.*;

/**
 * <h1>SettingsMenu</h1>
 * The menu for all GUI-specific settings.
 */
public class SettingsMenu extends JMenu
{
    //private final PacketTypeMenu types = new PacketTypeMenu();
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
        //addSeparator();
        //add(types);
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

    /*
    /**
     * <h2>PacketTypeMenu</h2>
     * A menu for blacklisted packets in the Supervisor GUI.
     *\/
    public static class PacketTypeMenu extends JMenu
    {
        public PacketTypeMenu()
        {
            super("Ignored Packets");

            // For every theme, build a radio button for it.
            for (PacketType type : PacketType.values())
            {
                JCheckBoxMenuItem themeItem = new JCheckBoxMenuItem();
                //--
                if (SupervisorGUI.CONFIG.getBlacklistedTypes().contains(type))
                {
                    themeItem.setSelected(true);
                }
                //--
                themeItem.addActionListener((event) -> {
                    if (SupervisorGUI.CONFIG.getBlacklistedTypes().contains(type))
                        SupervisorGUI.CONFIG.getBlacklistedTypes().remove(type);
                    else
                        SupervisorGUI.CONFIG.getBlacklistedTypes().add(type);
                });
                themeItem.setText(type.getName());
                //--
                add(themeItem);
            }
        }
    }
    */
}