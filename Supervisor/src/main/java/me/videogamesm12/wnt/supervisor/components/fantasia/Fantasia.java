package me.videogamesm12.wnt.supervisor.components.fantasia;

import lombok.Getter;
import me.videogamesm12.wnt.supervisor.api.SVComponent;
import net.kyori.adventure.key.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>Fantasia</h1>
 * <p>A component for the Supervisor that allows you to interface with it with a Telnet client.</p>
 */
public class Fantasia implements SVComponent
{
    @Getter
    private static Logger serverLogger = LoggerFactory.getLogger("Fantasia-Server");
    @Getter
    private static Fantasia instance;
    //--
    private final Key identifier = Key.key("wnt", "fantasia");
    //--
    @Getter
    private Server server;

    @Override
    public Key identifier()
    {
        return identifier;
    }

    @Override
    public void setup()
    {
        instance = this;
        //--
        serverLogger.info("Starting Fantasia server...");
        server = new Server();
        server.start();
    }

    @Override
    public void shutdown()
    {
        server.shutdown();
    }
}
