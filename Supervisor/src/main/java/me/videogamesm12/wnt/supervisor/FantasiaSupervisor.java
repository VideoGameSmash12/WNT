package me.videogamesm12.wnt.supervisor;

import lombok.Getter;
import me.videogamesm12.wnt.supervisor.api.SVComponent;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.flags.Flags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class FantasiaSupervisor extends Thread
{
    private static Logger logger = LoggerFactory.getLogger("Fantasia");
    @Getter
    private static FantasiaSupervisor instance;
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
        FabricLoader.getInstance().getEntrypoints("wnt-supervisor", SVComponent.class).forEach(component ->
        {
            try
            {
                CompletableFuture.runAsync(component::setup);
            }
            catch (Throwable ex)
            {
                logger.warn("Failed to set up component " + component.getClass().getName(), ex);
            }
        });
        logger.info("Components successfully set up.");
    }

    public static void setup()
    {
        instance = new FantasiaSupervisor();
        instance.start();
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
