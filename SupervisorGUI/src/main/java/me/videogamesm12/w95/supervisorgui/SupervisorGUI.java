package me.videogamesm12.w95.supervisorgui;

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
import me.videogamesm12.w95.Notifiable;
import me.videogamesm12.w95.W95;
import me.videogamesm12.w95.supervisor.event.ClientFreezeDetected;
import me.videogamesm12.w95.supervisorgui.menus.MitigationsMenu;
import me.videogamesm12.w95.supervisorgui.menus.SettingsMenu;
import me.videogamesm12.w95.supervisorgui.menus.ToolsMenu;
import me.videogamesm12.w95.supervisorgui.tabs.EntityTab;
import me.videogamesm12.w95.supervisorgui.tabs.PlayersTab;
import me.videogamesm12.w95.supervisorgui.tabs.SupervisorTab;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class SupervisorGUI extends Thread implements ModInitializer, ClientLifecycleEvents.ClientStopping, ClientFreezeDetected
{
    public static GUIConfig CONFIG = null;
    public static GUIFrame GUI = null;
    //--
    private boolean ignore;

    @Override
    public void onInitialize()
    {
        System.setProperty("java.awt.headless", "false");
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

        int reaction = JOptionPane.showConfirmDialog(null, String.format("The Supervisor has detected a client-side freeze (last render was %sms ago).\nWould you like to open the Supervisor window?", now - lastRender), "Client Freeze Detected", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

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

    public static class GUIFrame extends JFrame implements KeyListener, Notifiable
    {
        // Menu Bar
        private JMenuBar menuBar;
        private JMenu w95Menu;
        private MitigationsMenu mitigationsMenu;
        private SettingsMenu settingsMenu;
        private ToolsMenu toolsMenu;
        //--
        private JTabbedPane tabs;
        //--
        private Timer timer = null;

        public GUIFrame()
        {
            setTitle("Supervisor GUI");
            try
            {
                // Loads the icon from disk.
                InputStream iconStream = SupervisorGUI.class.getClassLoader().getResourceAsStream("assets/w95-supervisorgui/supervisor_icon_128.png");
                setIconImage(ImageIO.read(iconStream));
            }
            catch (Exception ex)
            {
                W95.LOGGER.error(ex);
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
            w95Menu = new JMenu("W95");
            mitigationsMenu = new MitigationsMenu();
            toolsMenu = new ToolsMenu();
            settingsMenu = new SettingsMenu();
            //--
            menuBar.add(w95Menu);
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
            //--
            if (SupervisorGUI.CONFIG.autoUpdate())
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

        @Override
        public void sendNotification(Text text, NotificationType type)
        {
            int messageType = type == NotificationType.ERROR ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
            JOptionPane.showMessageDialog(this, type.getTitle(), text.asString(), messageType);
        }
    }

    @Config(name = "w95-supervisorgui")
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
                    W95.LOGGER.error(ex);
                }
            }
        }
    }
}
