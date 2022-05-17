package me.videogamesm12.wnt.blackbox;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.commands.BlackboxCommand;
import me.videogamesm12.wnt.command.CommandSystem;
import me.videogamesm12.wnt.supervisor.event.ClientFreezeDetected;
import me.videogamesm12.wnt.blackbox.menus.MitigationsMenu;
import me.videogamesm12.wnt.blackbox.menus.SettingsMenu;
import me.videogamesm12.wnt.blackbox.menus.ToolsMenu;
import me.videogamesm12.wnt.blackbox.tabs.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.Timer;

public class Blackbox extends Thread implements ModInitializer, ClientLifecycleEvents.ClientStopping, ClientFreezeDetected
{
    public static GUIConfig CONFIG = null;
    public static GUIFrame GUI = null;
    public static GUIMenu MENU = null;
    //--
    public static TrayIcon trayIcon = null;
    //--
    private boolean ignore;

    @Override
    public void onInitialize()
    {
        System.setProperty("java.awt.headless", "false");
        //--
        CommandSystem.registerCommand(BlackboxCommand.class);
        //--
        start();
    }

    @Override
    public void run()
    {
        ClientLifecycleEvents.CLIENT_STOPPING.register(this);
        ClientFreezeDetected.EVENT.register(this);

        AutoConfig.register(GUIConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(GUIConfig.class).getConfig();

        CONFIG.theme.apply();

        if (CONFIG.showOnStartup())
        {
            GUI = new GUIFrame();
            GUI.setVisible(true);
        }

        try
        {
            MENU = new GUIMenu();

            if (SystemTray.isSupported())
            {
                SystemTray tray = SystemTray.getSystemTray();
                trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(
                        Blackbox.class.getClassLoader().getResource("assets/wnt-blackbox/supervisor_icon.png")), "WNT");
                trayIcon.setImageAutoSize(true);
                trayIcon.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        MENU.setLocation(e.getLocationOnScreen());
                        MENU.setInvoker(MENU);
                        MENU.setVisible(true);
                    }
                });
                tray.add(trayIcon);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClientStopping(MinecraftClient client)
    {
        AutoConfig.getConfigHolder(GUIConfig.class).save();
    }

    @Override
    public ActionResult onClientFreeze(long lastRender)
    {
        if (ignore || (GUI != null && GUI.isVisible()))
        {
            return ActionResult.PASS;
        }

        long now = Instant.now().toEpochMilli();

        int reaction = JOptionPane.showConfirmDialog(null, String.format("The Supervisor has detected a client-side freeze (last render was %sms ago).\nWould you like to open the Blackbox?", now - lastRender), "Client Freeze Detected", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        switch (reaction)
        {
            // The user clicked the Yes button
            case JOptionPane.YES_OPTION:
            {
                if (GUI == null)
                {
                    GUI = new GUIFrame();
                }

                GUI.setVisible(true);
                break;
            }

            /*  The user clicked the Cancel button. This does the same thing as the No button, but also ignores any
                future detections. */
            case JOptionPane.CANCEL_OPTION:
            {
                ignore = true;
            }

            // The user clicked the No button.
            case JOptionPane.NO_OPTION:
            {
                return ActionResult.FAIL;
            }

            // The user somehow clicked another button.
            default:
            {
                break;
            }
        }

        return ActionResult.PASS;
    }

    public static class GUIMenu extends JPopupMenu
    {
        private JMenuItem watermark = new JMenuItem("Blackbox");
        //--
        private JMenuItem openGui = new JMenuItem("Open Main Window");
        //--
        private MitigationsMenu mitigationsMenu;
        private SettingsMenu settingsMenu;
        private ToolsMenu toolsMenu;

        public GUIMenu()
        {
            watermark.setEnabled(false);
            //--
            openGui.addActionListener((event) -> {
                if (GUI == null)
                    GUI = new GUIFrame();

                GUI.setVisible(true);
            });
            //--
            mitigationsMenu = new MitigationsMenu();
            toolsMenu = new ToolsMenu();
            settingsMenu = new SettingsMenu();

            add(watermark);
            addSeparator();
            add(openGui);
            addSeparator();
            add(mitigationsMenu);
            add(toolsMenu);
            add(settingsMenu);

            // WHY DO I HAVE TO DO THIS
            addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseExited(MouseEvent e)
                {
                    setVisible(false);
                }
            });
        }
    }

    public static class GUIFrame extends JFrame implements KeyListener
    {
        // Menu Bar
        private JMenuBar menuBar;
        public JMenu wntMenu;
        private MitigationsMenu mitigationsMenu;
        private SettingsMenu settingsMenu;
        private ToolsMenu toolsMenu;
        //--
        private JTabbedPane tabs;
        //--
        private Timer timer = null;

        public GUIFrame()
        {
            setTitle("Blackbox");
            try
            {
                // Loads the icon from disk.
                InputStream iconStream = Blackbox.class.getClassLoader().getResourceAsStream("assets/wnt-blackbox/supervisor_icon_128.png");
                setIconImage(ImageIO.read(iconStream));
            }
            catch (Exception ex)
            {
                WNT.LOGGER.error(ex);
            }

            setMinimumSize(new Dimension(420, 560));
            setPreferredSize(new Dimension(420, 560));
            //--
            initComps();
            //--
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);
            pack();
        }

        private void initComps()
        {
            menuBar = new JMenuBar();
            wntMenu = new JMenu("WNT");
            mitigationsMenu = new MitigationsMenu();
            toolsMenu = new ToolsMenu();
            settingsMenu = new SettingsMenu();
            //--
            menuBar.add(wntMenu);
            menuBar.add(mitigationsMenu);
            menuBar.add(toolsMenu);
            menuBar.add(settingsMenu);
            //--
            tabs = new JTabbedPane();
            //--
            GroupLayout layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(tabs, GroupLayout.Alignment.TRAILING)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(tabs, GroupLayout.Alignment.TRAILING)
            );
            //--
            tabs.addTab("Players", new PlayersTab());
            tabs.addTab("Entities", new EntityTab());
            tabs.addTab("Maps", new MapsTab());
            //--
            if (Blackbox.CONFIG.autoUpdate())
            {
                scheduleRefresh();
            }
            //--
            setJMenuBar(menuBar);
        }

        /**
         * Disables the timer used for the automatic refresh.
         */
        public void cancelRefresh()
        {
            if (timer != null)
            {
                timer.cancel();
                timer = null;
            }
        }

        /**
         * Creates the timer used by the automatic refresh and schedules it.
         * @implNote If there is a timer already present, it kills that timer and starts anew.
         */
        public void scheduleRefresh()
        {
            if (timer != null)
                cancelRefresh();

            timer = new Timer();
            //--
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    ((SupervisorTab) tabs.getSelectedComponent()).update();
                }
            }, 0, 1000);
        }

        @Override
        public void keyTyped(KeyEvent e)
        {
            if (e.getKeyCode() == KeyEvent.VK_F5)
            {
                ((SupervisorTab) tabs.getTabComponentAt(tabs.getSelectedIndex())).update();
            }
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
        }

    }

    @Config(name = "wnt-blackbox")
    public static class GUIConfig implements ConfigData
    {
        private boolean showOnStartup;

        private boolean autoUpdate = true;

        @ConfigEntry.Gui.RequiresRestart
        private GUITheme theme = GUITheme.DARK;

        public GUITheme getTheme()
        {
            return theme;
        }

        public void setTheme(GUITheme theme)
        {
            this.theme = theme;
        }

        public boolean autoUpdate()
        {
            return autoUpdate;
        }

        public void setAutoUpdate(boolean value)
        {
            this.autoUpdate = value;
        }

        public boolean showOnStartup()
        {
            return showOnStartup;
        }

        public void setShowOnStartup(boolean value)
        {
            this.showOnStartup = value;
        }
    }

    /**
     * <h1>GUITheme</h1>
     * The method the GUI uses to get the selected theme.
     * --
     * TODO: Replace this with something more... modular.
     */
    public enum GUITheme
    {
        DARK("Material Darker", FlatMaterialDarkerIJTheme.class),
        DARK_HC("Material Darker (High Contrast)", FlatMaterialDarkerContrastIJTheme.class),
        LIGHT("Material Lighter", FlatMaterialLighterIJTheme.class),
        LIGHT_HC("Material Lighter (High Contrast)", FlatMaterialLighterContrastIJTheme.class),
        METAL("Metal", MetalLookAndFeel.class),
        NORD("Nord", FlatNordIJTheme.class),
        ONE_DARK("One Dark", FlatOneDarkIJTheme.class),
        PURPLE("Dark Purple", FlatDarkPurpleIJTheme.class);

        private Class<? extends BasicLookAndFeel> themeClass = null;
        private String themeName = null;

        GUITheme(String themeName, Class<? extends BasicLookAndFeel> themeClass)
        {
            this.themeName = themeName;
            this.themeClass = themeClass;
        }

        public String getName()
        {
            return themeName;
        }

        public void apply()
        {
            if (themeClass != null)
            {
                try
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
                catch (Exception ex)
                {
                    WNT.LOGGER.error(ex);
                }
            }
        }
    }
}
