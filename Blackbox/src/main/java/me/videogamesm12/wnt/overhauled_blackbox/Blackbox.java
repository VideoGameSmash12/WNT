package me.videogamesm12.wnt.overhauled_blackbox;

import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.CommandSystem;
import me.videogamesm12.wnt.overhauled_blackbox.command.BlackboxCommand;
import me.videogamesm12.wnt.overhauled_blackbox.theming.ThemeRegistry;
import me.videogamesm12.wnt.overhauled_blackbox.theming.inbuilt.IBThemes;
import me.videogamesm12.wnt.overhauled_blackbox.window.GUI;
import me.videogamesm12.wnt.overhauled_blackbox.window.SysTray;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.File;

public class Blackbox extends Thread implements ClientLifecycleEvents.ClientStarted, ClientLifecycleEvents.ClientStopping
{
    @Getter
    private static final Identifier identifier = new Identifier("wnt", "blackbox");
    @Getter
    private static Blackbox instance;
    //--
    public static void setup()
    {
        instance = new Blackbox();
        instance.start();
    }

    public static File getFolder()
    {
        return new File(WNT.getWNTFolderSafe(), "blackbox");
    }

    @Getter
    private Configuration config;

    @Getter
    private GUI mainWindow;

    @Getter
    private SysTray systemTrayIcon;

    @Override
    public void run()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register(this);
        ClientLifecycleEvents.CLIENT_STOPPING.register(this);
        Supervisor.getEventBus().register(this);

        config = Configuration.load();

        ThemeRegistry.setupThemes();
        try
        {
            ThemeRegistry.getTheme(config.getTheme()).apply();
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to apply selected theme", ex);
            ThemeRegistry.getTheme(IBThemes.METAL.getInternalName()).apply();
        }

        // Non-Linux operating systems open the window and set up the system tray icons earlier than Linux operating systems do.
        // If it wasn't like this, issues like this would happen:  https://github.com/VideoGameSmash12/WNT/issues/11
        if (Util.getOperatingSystem() != Util.OperatingSystem.LINUX)
        {
            startup();
        }

        // Registers commands in both WNT and Fantasia
        CommandSystem.registerCommand(BlackboxCommand.Minecraft.class);
        Fantasia.getInstance().getServer().registerCommand(BlackboxCommand.Fantasia.class);
    }
    
    @Override
    public void onClientStarted(MinecraftClient client)
    {
        // Linux operating systems open the window and set up the system tray icons later than non-Linux operating systems do.
        // If it wasn't like this, issues like this would happen:  https://github.com/VideoGameSmash12/WNT/issues/11
        if (Util.getOperatingSystem() == Util.OperatingSystem.LINUX)
        {
            startup();
        }
    }

    @Override
    public void onClientStopping(MinecraftClient client)
    {
        Configuration.save(config);
    }

    private void startup()
    {
        setupSystemTrayIcon();

        if (config.isShowOnStartupEnabled() && mainWindow == null)
        {
            openWindow();
        }
    }

    public void openWindow()
    {
        if (mainWindow == null)
        {
            mainWindow = new GUI();
        }

        mainWindow.setVisible(true);
    }

    public void setupSystemTrayIcon()
    {
        WNT.getLogger().info("Setting up system tray integration...");

        if (systemTrayIcon == null)
        {
            systemTrayIcon = new SysTray(this);

            try
            {
                systemTrayIcon.addIcon();
            }
            catch (Exception ex)
            {
                WNT.getLogger().warn("Failed to set up system tray integration", ex);
            }
        }
    }
}
