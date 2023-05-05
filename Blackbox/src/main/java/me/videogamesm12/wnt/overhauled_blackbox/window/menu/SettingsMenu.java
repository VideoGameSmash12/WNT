package me.videogamesm12.wnt.overhauled_blackbox.window.menu;

import me.videogamesm12.wnt.overhauled_blackbox.Blackbox;
import me.videogamesm12.wnt.overhauled_blackbox.window.menu.settings.ThemeMenu;

import javax.swing.*;

public class SettingsMenu extends JMenu
{
    public SettingsMenu()
    {
        super("Settings");
        //--
        JCheckBoxMenuItem autoRefresh = new JCheckBoxMenuItem("Auto-refresh", Blackbox.getInstance().getConfig().isAutoRefreshEnabled());
        autoRefresh.addActionListener(e -> Blackbox.getInstance().getConfig().setAutoRefreshEnabled(autoRefresh.isSelected()));
        JCheckBoxMenuItem showOnStartup = new JCheckBoxMenuItem("Show on start-up", Blackbox.getInstance().getConfig().isShowOnStartupEnabled());
        showOnStartup.addActionListener(e -> Blackbox.getInstance().getConfig().setShowOnStartupEnabled(showOnStartup.isSelected()));
        JCheckBoxMenuItem ignoreClientFreezes = new JCheckBoxMenuItem("Ignore client freezes on start-up", Blackbox.getInstance().getConfig().isIgnoringFreezesDuringStartup());
        ignoreClientFreezes.addActionListener(e -> Blackbox.getInstance().getConfig().setIgnoringFreezesDuringStartup(ignoreClientFreezes.isSelected()));
        //--
        JCheckBoxMenuItem enhancedLists = new JCheckBoxMenuItem("More detailed tabs", Blackbox.getInstance().getConfig().isEnhancedListingEnabled());
        enhancedLists.setToolTipText("Provides more information in the current tab you're on. Requires a restart to take effect.");
        enhancedLists.addActionListener(e ->
        {
            JOptionPane.showMessageDialog(this, "You will need to restart your Minecraft client for this to take effect.", "Restart Required", JOptionPane.INFORMATION_MESSAGE);
            Blackbox.getInstance().getConfig().setEnhancedListingEnabled(enhancedLists.isSelected());
        });
        //--
        add(new ThemeMenu());
        addSeparator();
        add(enhancedLists);
        addSeparator();
        add(autoRefresh);
        add(showOnStartup);
        add(ignoreClientFreezes);
    }
}
