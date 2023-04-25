package me.videogamesm12.wnt.supervisor.components.fantasia.listener;

import lombok.Getter;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.Server;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.UnixDomainSession;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnixDomainConnectionListener extends Thread implements IConnectionListener
{
    private static final Path socketPath = Path.of(System.getProperty("user.home")).resolve("wnt-fantasia.socket");;
    //--
    private final Server server;
    @Getter
    private final ServerSocketChannel channel;

    public UnixDomainConnectionListener(Server server) throws IOException
    {
        super("Fantasia-UnixSocketConnectionListener");
        this.server = server;
        //--
        this.channel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
        this.channel.bind(UnixDomainSocketAddress.of(socketPath));
    }


    @Override
    public void run()
    {
        while (channel.isOpen())
        {
            SocketChannel clientChannel;

            try
            {
                clientChannel = channel.accept();
            }
            catch (Exception ex)
            {
                continue;
            }

            UnixDomainSession session = new UnixDomainSession(server, clientChannel);
            server.addSession(session);
            session.start();
        }
    }

    @Override
    public void interrupt()
    {
        // It is absolutely important that we nuke this file when we shut down so that we don't have issues booting the next time.
        try
        {
            Files.deleteIfExists(socketPath);
        }
        catch (Exception ex)
        {
            Fantasia.getServerLogger().error("Failed to delete socket file", ex);
        }

        super.interrupt();
    }

    @Override
    public void shutdown()
    {
        try
        {
            channel.close();
        }
        catch (Exception ignored)
        {
        }

        try
        {
            Files.deleteIfExists(socketPath);
        }
        catch (Exception ex)
        {
            Fantasia.getServerLogger().error("Failed to delete socket file", ex);
        }
    }
}
