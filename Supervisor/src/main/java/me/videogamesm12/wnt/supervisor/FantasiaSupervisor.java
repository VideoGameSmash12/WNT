package me.videogamesm12.wnt.supervisor;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import me.videogamesm12.wnt.supervisor.api.SVComponent;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.flags.Flags;
import me.videogamesm12.wnt.supervisor.components.watchdog.Watchdog;
import me.videogamesm12.wnt.util.Messenger;
import net.kyori.adventure.text.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FantasiaSupervisor extends Thread
{
    @Getter
    private static final EventBus eventBus = new EventBus();
    //
    private static Logger logger = LoggerFactory.getLogger("Fantasia");
    @Getter
    private static FantasiaSupervisor instance;
    //--
    private final List<SVComponent> components = new ArrayList<>();
    //--
    @Getter
    private final Flags flags;

    public FantasiaSupervisor()
    {
        super("Supervisor");
        //--
        logger.info("Setting up the Supervisor...");
        instance = this;
        //--
        this.flags = new Flags();

        logger.info("Setting up components...");
        components.add(new Fantasia());
        components.add(new Watchdog());
        components.forEach(SVComponent::setup);
        logger.info("Components successfully set up.");
    }

    public static void setup()
    {
        instance = new FantasiaSupervisor();
        instance.start();
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
}
