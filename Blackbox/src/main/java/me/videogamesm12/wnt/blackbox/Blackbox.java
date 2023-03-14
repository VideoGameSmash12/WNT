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

package me.videogamesm12.wnt.blackbox;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
//import com.google.common.eventbus.Subscribe;
import com.formdev.flatlaf.util.SystemInfo;
import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.commands.BlackboxCommand;
import me.videogamesm12.wnt.blackbox.menus.*;
import me.videogamesm12.wnt.command.CommandSystem;
//import me.videogamesm12.wnt.dumper.events.DumpResultEvent;
import me.videogamesm12.wnt.supervisor.event.ClientFreezeDetected;
import me.videogamesm12.wnt.blackbox.tabs.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.Timer;

public class Blackbox extends Thread implements ModInitializer, ClientLifecycleEvents.ClientStarted,
        ClientLifecycleEvents.ClientStopping, ClientFreezeDetected
{
    public static final Identifier IDENTIFIER = Identifier.of("wnt", "blackbox");
    //--
    public static GUIConfig CONFIG = null;
    public static GUIFrame GUI = null;
    public static GUIMenu MENU = null;
    //--
    public static TrayIcon trayIcon = null;
    //--
    private boolean started = false;
    private boolean ignore;

    @Override
    public void onInitialize()
    {
        switch (Util.getOperatingSystem())
        {
            case SOLARIS, UNKNOWN ->
            {
                WNT.getLogger().warn("The Blackbox has not been properly tested under this operating system, so in the "
                        + "interest of maintaining client stability, it has been disabled.");
                return;
            }
            case LINUX, OSX ->
            {
                // https://bugs.openjdk.org/browse/JDK-8056151
                System.setProperty("sun.java2d.xrender", "f");
            }
        }

        System.setProperty("java.awt.headless", "false");
        CommandSystem.registerCommand(BlackboxCommand.class);

        start();
    }

    @Override
    public void run()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register(this);
        ClientLifecycleEvents.CLIENT_STOPPING.register(this);
        ClientFreezeDetected.EVENT.register(this);

        AutoConfig.register(GUIConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(GUIConfig.class).getConfig();

        CONFIG.theme.apply();

        // Non-Linux operating systems open the window earlier than Linux operating systems do.
        // If it wasn't like this, this issue would happen: https://github.com/VideoGameSmash12/WNT/issues/11
        if (CONFIG.showOnStartup() && GUI == null && Util.getOperatingSystem() != Util.OperatingSystem.LINUX)
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

    // Linux operating systems open the window later than non-Linux operating systems do.
    // If it wasn't like this, this issue would happen: https://github.com/VideoGameSmash12/WNT/issues/11
    @Override
    public void onClientStarted(MinecraftClient client)
    {
        started = true;

        if (CONFIG.showOnStartup() && GUI == null && Util.getOperatingSystem() == Util.OperatingSystem.LINUX)
        {
            GUI = new GUIFrame();
            GUI.setVisible(true);
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
        if (ignore || (CONFIG.ignoreFreezesDuringStartup() && !started) || (GUI != null && GUI.isVisible()))
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
        @Getter
        private WNTMenu wntMenu;
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
                InputStream iconStream = Blackbox.class.getClassLoader().getResourceAsStream("assets/wnt-blackbox/icon.png");
                setIconImage(ImageIO.read(iconStream));
            }
            catch (Exception ex)
            {
                WNT.getLogger().error("Failed to load icon image", ex);
            }

            setMinimumSize(new Dimension(420, 560));
            setPreferredSize(new Dimension(420, 560));
            //--
            initComps();
            //--
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);
            pack();
            //--
            //WNT.getEventBus().register(this);
        }

        private void initComps()
        {
            menuBar = new JMenuBar();
            wntMenu = new WNTMenu();
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
            tabs.addTab("General", new GeneralTab());
            tabs.addTab("Players", new PlayersTab());
            tabs.addTab("Entities", new EntityTab());
            tabs.addTab("Maps", new MapsTab());
            //--
            if (Blackbox.CONFIG.autoUpdate())
                scheduleRefresh();
            //--
            tabs.addKeyListener(this);
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
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == KeyEvent.VK_F5)
                ((SupervisorTab) tabs.getSelectedComponent()).update();
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
        }

        /*@Subscribe
        public void onDumpResult(DumpResultEvent event)
        {
            if (event.getRequester().equals(IDENTIFIER))
            {
                String title = event.getResult() == ActionResult.SUCCESS ? "Success" : "Error";
                int messageType = event.getResult() == ActionResult.SUCCESS ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

                JOptionPane.showMessageDialog(null, event.getMessage().toString(), title, messageType);
            }
        }*/
    }

    @Config(name = "wnt-blackbox")
    public static class GUIConfig implements ConfigData
    {
        private boolean showOnStartup;
        private boolean ignoreFreezesDuringStartup = true;

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

        public boolean ignoreFreezesDuringStartup()
        {
            return ignoreFreezesDuringStartup;
        }

        public void setIgnoreFreezesDuringStartup(boolean bool)
        {
            this.ignoreFreezesDuringStartup = bool;
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

    public static File getBlackboxFolder()
    {
        File folder = new File(WNT.getWNTFolder(), "blackbox");

        if (!folder.exists())
        {
            folder.mkdirs();
        }

        return folder;
    }

    public enum GUIThemeType
    {
        BUILT_IN("Built into Java"),
        FLATLAF("Built into FlatLAF");

        @Getter
        private String label;

        GUIThemeType(String label)
        {
            this.label = label;
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
        ARC_DARK("Arc Dark", GUIThemeType.FLATLAF, FlatArcDarkIJTheme.class, true),
        ARC_DARK_HC("Arc Dark Contrast", "A variant of Arc Dark with better text box contrast.", GUIThemeType.FLATLAF, FlatArcDarkContrastIJTheme.class, true),
        CARBON("Carbon", GUIThemeType.FLATLAF, FlatCarbonIJTheme.class, true),
        COBALT_2("Cobalt 2", GUIThemeType.FLATLAF, FlatCobalt2IJTheme.class, true),
        CUSTOM("Custom", "Loads a theme from .minecraft/wnt/blackbox/theme.json.", GUIThemeType.FLATLAF, FlatMaterialDarkerIJTheme.class, true),
        DARK("Material Darker", GUIThemeType.FLATLAF, FlatMaterialDarkerIJTheme.class, true),
        DARK_HC("Material Darker Contrast", "A variant of Material Darker with better text box contrast.", GUIThemeType.FLATLAF, FlatMaterialDarkerContrastIJTheme.class, true),
        LIGHT("Material Lighter", GUIThemeType.FLATLAF, FlatMaterialLighterIJTheme.class, true),
        LIGHT_HC("Material Lighter Contrast", "A variant of Material Lighter with better text box contrast.", GUIThemeType.FLATLAF, FlatMaterialLighterContrastIJTheme.class, true),
        DEEP_OCEAN("Material Deep Ocean", GUIThemeType.FLATLAF, FlatMaterialDeepOceanIJTheme.class, true),
        DEEP_OCEAN_HC("Material Deep Ocean Contrast", "A variant of Material Deep Ocean with better text box contrast.", GUIThemeType.FLATLAF, FlatMaterialDeepOceanContrastIJTheme.class, true),
        NORD("Nord", GUIThemeType.FLATLAF, FlatNordIJTheme.class, true),
        ONE_DARK("One Dark", GUIThemeType.FLATLAF, FlatOneDarkIJTheme.class, true),
        PURPLE("Dark Purple", GUIThemeType.FLATLAF, FlatDarkPurpleIJTheme.class, true),
        //--
        METAL("Metal", GUIThemeType.BUILT_IN, MetalLookAndFeel.class, true),
        MOTIF("Motif", "A hilariously outdated theme that hasn't changed at all since the 1990s.", GUIThemeType.BUILT_IN, "com.sun.java.swing.plaf.motif.MotifLookAndFeel", true),
        SYSTEM("System", "A theme that automatically adapts to whatever operating system you are currently using.", GUIThemeType.BUILT_IN, UIManager.getSystemLookAndFeelClassName(), true),
        WINDOWS("Windows", "Ah yes, good ol' Win32.", GUIThemeType.BUILT_IN, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", SystemInfo.isWindows),
        WINDOWS_CLASSIC("Windows Classic", "Perfect for those who prefer function over form.", GUIThemeType.BUILT_IN, "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel", SystemInfo.isWindows);

        private String themeName = null;
        private String themeDescription = null;
        private Class<? extends BasicLookAndFeel> themeClass = null;
        private GUIThemeType type = null;
        private String internalPackage = null;
        private boolean shouldShow = false;

        GUITheme(String themeName, String themeDescription, GUIThemeType type, Class<? extends BasicLookAndFeel> themeClass, boolean shouldShow)
        {
            this.themeName = themeName;
            this.themeDescription = themeDescription;
            this.type = type;
            this.themeClass = themeClass;
            this.shouldShow = shouldShow;
        }

        GUITheme(String themeName, String themeDescription, GUIThemeType type, String internalPackage, boolean shouldShow)
        {
            this.themeName = themeName;
            this.themeDescription = themeDescription;
            this.type = type;
            this.internalPackage = internalPackage;
            this.shouldShow = shouldShow;
        }

        GUITheme(String themeName, GUIThemeType type, Class<? extends BasicLookAndFeel> themeClass, boolean shouldShow)
        {
            this.themeName = themeName;
            this.type = type;
            this.themeClass = themeClass;
            this.shouldShow = shouldShow;
        }

        GUITheme(String themeName, GUIThemeType type, String internalPackage, boolean shouldShow)
        {
            this.themeName = themeName;
            this.type = type;
            this.internalPackage = internalPackage;
            this.shouldShow = shouldShow;
        }

        public String getName()
        {
            return themeName;
        }

        public String getDescription()
        {
            return themeDescription;
        }

        public GUIThemeType getThemeType()
        {
            return type;
        }

        public boolean shouldShow()
        {
            return shouldShow;
        }


        public void showOptionalChangeMessage()
        {
            if (this == CUSTOM)
            {
                JOptionPane.showMessageDialog(null, "Just so you know, the custom theme will need to be located at .minecraft/wnt/blackbox/theme.json with this theme enabled.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        public void apply()
        {
            try
            {
                // We are using a custom theme for FlatLAF.
                if (this == CUSTOM)
                {
                    File file = new File(getBlackboxFolder(), "theme.json");
                    if (!file.exists())
                    {
                        // Fallback
                        FlatMaterialDarkerIJTheme.setup();
                        WNT.getLogger().warn("You set your theme for the Blackbox to Custom, but no theme was found at .minecraft/wnt/blackbox/theme.json. Please put a theme JSON file there.");
                    }
                    else
                    {
                        IntelliJTheme.setup(new FileInputStream(file));
                    }
                }
                // We are using something that belongs to FlatLAF.
                else if (themeClass != null)
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
                // We are using something that isn't FlatLAF.
                else if (internalPackage != null)
                {
                    UIManager.setLookAndFeel(internalPackage);
                }
            }
            catch (Exception ex)
            {
                WNT.getLogger().error("Failed to apply currently active theme", ex);
            }
        }
    }
}
