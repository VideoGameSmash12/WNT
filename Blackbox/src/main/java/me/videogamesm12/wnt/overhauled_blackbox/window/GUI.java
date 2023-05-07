package me.videogamesm12.wnt.overhauled_blackbox.window;

import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.overhauled_blackbox.Blackbox;
import me.videogamesm12.wnt.overhauled_blackbox.window.general.Dynamic;
import me.videogamesm12.wnt.overhauled_blackbox.window.menu.MitigationsMenu;
import me.videogamesm12.wnt.overhauled_blackbox.window.menu.SettingsMenu;
import me.videogamesm12.wnt.overhauled_blackbox.window.menu.ToolsMenu;
import me.videogamesm12.wnt.overhauled_blackbox.window.menu.WNTMenu;
import me.videogamesm12.wnt.overhauled_blackbox.window.tab.EntitiesTab;
import me.videogamesm12.wnt.overhauled_blackbox.window.tab.MainTab;
import me.videogamesm12.wnt.overhauled_blackbox.window.tab.MapsTab;
import me.videogamesm12.wnt.overhauled_blackbox.window.tab.PlayersTab;
import me.videogamesm12.wnt.overhauled_blackbox.window.tool.console.Console;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame
{
    @Getter
    private Console console;
    //--
    private final JMenuBar menuBar;
    private final JTabbedPane tabbedPane;
    //--
    private final Timer timer;

    public GUI()
    {
        // Window basics
        setTitle("Blackbox");
        setName("Blackbox");
        setupIcon();

        // Sets up the window dimensions
        setMinimumSize(new Dimension(420, 560));
        setPreferredSize(new Dimension(420, 560));

        // Sets up the components
        menuBar = new JMenuBar();
        menuBar.add(new WNTMenu());
        menuBar.add(new MitigationsMenu());
        menuBar.add(new SettingsMenu());
        menuBar.add(new ToolsMenu());
        add(menuBar);
        setJMenuBar(menuBar);
        //--
        tabbedPane = new JTabbedPane();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabbedPane,
                GroupLayout.Alignment.TRAILING));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabbedPane,
                GroupLayout.Alignment.TRAILING));
        // TODO: Consider adding icons for each tab. It might make the UI look fancier.
        tabbedPane.addTab("General", new MainTab());
        tabbedPane.addTab("Players", new PlayersTab());
        tabbedPane.addTab("Entities", new EntitiesTab());
        tabbedPane.addTab("Maps", new MapsTab());
        // End of component setup

        // Sets up timers
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                // If the window is not visible, then don't do anything
                if (!isVisible())
                {
                    return;
                }

                // Automatically updates menus if they can be updated
                for (int i = 0; i < menuBar.getMenuCount(); i++)
                {
                    JMenu menu = menuBar.getMenu(i);

                    if (menu instanceof Dynamic dynamic)
                    {
                        dynamic.update();
                    }
                }

                // Automatically "refreshes" the current tab if enabled and if it can even be updated
                if (Blackbox.getInstance().getConfig().isAutoRefreshEnabled()
                        && tabbedPane.getSelectedComponent() instanceof Dynamic dynamicTab)
                {
                    dynamicTab.update();
                }
            }
        }, 0, 1000);

        // Finally, we show the stuff now.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);
        pack();
    }

    public void openConsoleWindow()
    {
        if (console == null)
        {
            console = new Console();
        }

        console.setVisible(true);
    }

    public void setupIcon()
    {
        try
        {
            // Loads the icon from disk.
            InputStream iconStream = Blackbox.class.getClassLoader().getResourceAsStream("assets/wnt-blackbox/icon.png");
            setIconImage(ImageIO.read(iconStream));
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to load icon image", ex);
        }
    }
}
