package me.videogamesm12.wnt.overhauled_blackbox.window.menu.settings;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.overhauled_blackbox.Blackbox;
import me.videogamesm12.wnt.overhauled_blackbox.theming.ITheme;
import me.videogamesm12.wnt.overhauled_blackbox.theming.ThemeRegistry;

import javax.swing.*;
import java.util.Comparator;

/**
 * <h1>ThemeMenu</h1>
 * A menu for the theme selection in the Blackbox.
 */
public class ThemeMenu extends JMenu
{
    private final ButtonGroup group = new ButtonGroup();

    public ThemeMenu()
    {
        super("Theme");

        ThemeRegistry.getThemeTypes().forEach(type ->
        {
            JMenuItem label = new JMenuItem("--== " + type.getLabel() + " ==--");
            label.setEnabled(false);
            add(label);

            if (ThemeRegistry.getThemes().entrySet().stream().noneMatch(theme -> theme.getValue().getType().getId() == type.getId() && theme.getValue().isSupposedToShow()))
            {
                JMenuItem emptyItem = new JMenuItem("(none)");
                emptyItem.setEnabled(false);
                add(emptyItem);
                return;
            }

            ThemeRegistry.getThemes().entrySet().stream().filter(theme -> theme.getValue().getType().getId() == type.getId() && theme.getValue().isSupposedToShow()).sorted(Comparator.comparing(set -> set.getValue().getThemeName())).forEach(set ->
            {
                String themeId = set.getKey();
                ITheme theme = set.getValue();
                //--
                JRadioButtonMenuItem themeItem = new JRadioButtonMenuItem();
                themeItem.setText(theme.getThemeName());
                themeItem.setToolTipText(theme.getThemeDescription());
                themeItem.setSelected(Blackbox.getInstance().getConfig().getTheme().equalsIgnoreCase(themeId));
                themeItem.addActionListener((e) ->
                {
                    WNT.getLogger().info("Switching theme to " + theme.getThemeName() + "...");
                    //--
                    ITheme originalTheme = ThemeRegistry.getTheme(Blackbox.getInstance().getConfig().getTheme());
                    Blackbox.getInstance().getConfig().setTheme(themeId);
                    //--
                    theme.apply();
                    theme.getType().update();
                    //--
                    if (originalTheme.getType().getId() != theme.getType().getId())
                    {
                        JOptionPane.showMessageDialog(this, "If things end up looking broken, try rebooting your Minecraft client.", "Notice", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                group.add(themeItem);
                add(themeItem);
            });
        });
    }
}