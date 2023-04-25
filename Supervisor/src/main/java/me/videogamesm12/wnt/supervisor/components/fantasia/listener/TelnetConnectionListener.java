package me.videogamesm12.wnt.supervisor.components.fantasia.listener;

import lombok.Getter;
import me.videogamesm12.wnt.supervisor.components.fantasia.Server;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.TelnetSession;

import java.net.ServerSocket;
import java.net.Socket;

public class TelnetConnectionListener extends Thread implements IConnectionListener
{
    private final Server server;
    @Getter
    private final ServerSocket socket;

    public TelnetConnectionListener(Server server, ServerSocket socket)
    {
        super("Fantasia-SocketListener");
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        while (!socket.isClosed())
        {
            Socket clientSocket;

            try
            {
                clientSocket = socket.accept();
            }
            catch (Exception ignored)
            {
                continue;
            }

            TelnetSession session = new TelnetSession(server, clientSocket);
            server.addSession(session);
            session.start();
        }
    }

    @Override
    public void shutdown()
    {
        try
        {
            getSocket().close();
        }
        catch (Exception ignored)
        {
        }
    }
}
