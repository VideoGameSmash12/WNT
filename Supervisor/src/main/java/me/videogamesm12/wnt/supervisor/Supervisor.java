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

package me.videogamesm12.wnt.supervisor;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.videogamesm12.wnt.supervisor.api.SVComponent;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.flags.Flags;
import me.videogamesm12.wnt.supervisor.components.watchdog.Watchdog;
import me.videogamesm12.wnt.supervisor.mixin.gui.DebugHudMixin;
import me.videogamesm12.wnt.supervisor.mixin.gui.InGameHudMixin;
import me.videogamesm12.wnt.supervisor.util.Fallbacks;
import me.videogamesm12.wnt.util.Messenger;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <h1>Supervisor</h1>
 * A major component in WNT, offering better control over the client
 */
public class Supervisor extends Thread
{
    @Getter
    private static final EventBus eventBus = new EventBus();
    //
    private static Logger logger = LoggerFactory.getLogger("Fantasia");
    @Getter
    private static Supervisor instance;
    @Getter
    private static Configuration config;
    //--
    private final List<SVComponent> components = new ArrayList<>();
    @Getter
    private final Flags flags;

    public Supervisor()
    {
        super("Supervisor");
        this.flags = new Flags();
    }

    public static void setup()
    {
        instance = new Supervisor();
        instance.start();
    }

    @Override
    public void run()
    {
        logger.info("Setting up the Supervisor...");
        instance = this;
        //--
        logger.info("Loading configuration...");
        config = loadConfiguration();
        //--
        logger.info("Setting up components...");
        components.add(new Fantasia());
        components.add(new Watchdog());
        components.forEach(SVComponent::setup);
        logger.info("Components successfully set up.");
    }

    public Configuration loadConfiguration()
    {
        File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "wnt-supervisor.json");

        if (file.exists())
        {
            try
            {
                return new Gson().fromJson(new FileReader(file), Configuration.class);
            }
            catch (Exception ex)
            {
                logger.error("Failed to read Supervisor configuration", ex);
                return new Configuration();
            }
        }
        else
        {
            return new Configuration();
        }
    }

    public void saveConfiguration()
    {
        File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "wnt-supervisor.json");
        try (FileWriter writer = new FileWriter(file))
        {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
        }
        catch (Exception ex)
        {
            logger.error("Failed to write Supervisor configuration", ex);
        }
    }

    public void chatMessage(String message)
    {
        if (!getFlags().isGameStartedYet())
        {
            throw new IllegalStateException("The Minecraft client hasn't finished starting up yet.");
        }

        if (MinecraftClient.getInstance().getNetworkHandler() == null)
        {
            throw new IllegalStateException("You are not connected to a server.");
        }

        MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(message);
    }

    public void disconnect()
    {
        if (!getFlags().isGameStartedYet())
        {
            throw new IllegalStateException("The Minecraft client hasn't finished starting up yet.");
        }

        if (MinecraftClient.getInstance().getNetworkHandler() == null)
        {
            throw new IllegalStateException("You are not connected to a server.");
        }

        // TODO: This is utterly retarded, but 1.19.4 has forced my hand. Fix this when we eventually drop support for 1.19.3.
        ClientConnection connection;
        try
        {
            connection = MinecraftClient.getInstance().getNetworkHandler().getConnection();
        }
        catch (NoSuchMethodError ex)
        {
            try
            {
                connection = (ClientConnection) ClientPlayNetworkHandler.class.getMethod("method_48296").invoke(MinecraftClient.getInstance().getNetworkHandler());
            }
            catch (Exception exception)
            {
                return;
            }
        }

        connection.disconnect(Messenger.convert(Component.text("Disconnected by Supervisor")));
    }

    public void runCommand(String command)
    {
        if (!getFlags().isGameStartedYet())
        {
            throw new IllegalStateException("The Minecraft client hasn't finished starting up yet.");
        }

        if (MinecraftClient.getInstance().getNetworkHandler() == null)
        {
            throw new IllegalStateException("You are not connected to a server.");
        }

        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand(command);
    }

    public void shutdown()
    {
        saveConfiguration();
        components.forEach(SVComponent::shutdown);
    }

    public void shutdownForcefully()
    {
        logger.info("Shutting down forcefully!");
        System.exit(42069);
    }

    public void shutdownSafely()
    {
        logger.info("Shutting down safely!");

        if (!getFlags().isGameStartedYet())
        {
            throw new IllegalStateException("The Minecraft client hasn't even finished starting up yet.");
        }

        MinecraftClient.getInstance().scheduleStop();
    }

    public String getFPSText()
    {
        if (!getFlags().isGameStartedYet())
        {
            throw new IllegalStateException("The Minecraft client hasn't finished starting up yet.");
        }

        return MinecraftClient.getInstance().fpsDebugString;
    }

    public List<String> getF3Info()
    {
        synchronized (this)
        {
            try
            {
                return ((DebugHudMixin) ((InGameHudMixin) MinecraftClient.getInstance().inGameHud).getDebugHud()).getLeftText();
            }
            catch (Exception | Error ex)
            {
                return Fallbacks.getLeftText();
            }
        }
    }
}
