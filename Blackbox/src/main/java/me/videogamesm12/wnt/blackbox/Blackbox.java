/*
 * Copyright (c) 2023 Video
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

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.menus.*;
import me.videogamesm12.wnt.blackbox.theming.ThemeRegistry;
import me.videogamesm12.wnt.blackbox.theming.inbuilt.IBThemes;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.api.event.ClientFreezeEvent;
import me.videogamesm12.wnt.blackbox.tabs.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.Timer;

@Deprecated(forRemoval = true)
public class Blackbox extends Thread implements ModInitializer, ClientLifecycleEvents.ClientStarted,
        ClientLifecycleEvents.ClientStopping
{
    public static GUIConfig CONFIG = null;
    public static GUIFrame GUI = null;
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

        start();
    }

    @Override
    public void run()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register(this);
        ClientLifecycleEvents.CLIENT_STOPPING.register(this);
        Supervisor.getEventBus().register(this);

        AutoConfig.register(GUIConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(GUIConfig.class).getConfig();

        ThemeRegistry.setupThemes();
        try
        {
            ThemeRegistry.getTheme(CONFIG.getTheme()).apply();
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to apply selected theme", ex);
            ThemeRegistry.getTheme(IBThemes.METAL.getInternalName()).apply();
        }

        // Non-Linux operating systems open the window earlier than Linux operating systems do.
        // If it wasn't like this, this issue would happen: https://github.com/VideoGameSmash12/WNT/issues/11
        if (CONFIG.showOnStartup() && GUI == null && Util.getOperatingSystem() != Util.OperatingSystem.LINUX)
        {
            GUI = new GUIFrame();
            GUI.setVisible(true);
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

    @Subscribe
    public void onClientFreeze(ClientFreezeEvent event)
    {
        if (ignore || (CONFIG.ignoreFreezesDuringStartup() && !started) || (GUI != null && GUI.isVisible()))
        {
            return;
        }

        long now = Instant.now().toEpochMilli();

        int reaction = JOptionPane.showConfirmDialog(null, String.format("The Supervisor has detected a client-side freeze (last render was %sms ago).\nWould you like to open the Blackbox?", now - event.getLastRendered()), "Client Freeze Detected", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        switch (reaction)
        {
            // The user clicked the Yes button
            case JOptionPane.YES_OPTION ->
            {
                if (GUI == null)
                {
                    GUI = new GUIFrame();
                }

                GUI.setVisible(true);
            }

            /*  The user clicked the Cancel button. This does the same thing as the No button, but also ignores any
                future detections. */
            case JOptionPane.CANCEL_OPTION ->
            {
                ignore = true;
            }

            // The user clicked the No button. Do nothing.
            case JOptionPane.NO_OPTION ->
            {
            }
        }
    }

    public static class GUIFrame extends JFrame implements KeyListener
    {
        // Menu Bar
        private JMenuBar menuBar;
        private MitigationsMenu mitigationsMenu;
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
            mitigationsMenu = new MitigationsMenu();
            toolsMenu = new ToolsMenu();
            //--
            menuBar.add(mitigationsMenu);
            menuBar.add(toolsMenu);
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

        @ConfigEntry.Gui.Excluded
        private String theme = "DARK";

        public String getTheme()
        {
            return theme;
        }

        public void setTheme(String theme)
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
}
