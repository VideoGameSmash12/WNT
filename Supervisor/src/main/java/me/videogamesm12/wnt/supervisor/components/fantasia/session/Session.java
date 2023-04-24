package me.videogamesm12.wnt.supervisor.components.fantasia.session;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.Server;

import java.io.*;
import java.net.Socket;

public class Session extends Thread
{
    private Server server;
    private final Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private CommandSender sender;

    public Session(Server server, Socket socket)
    {
        this.socket = socket;
        this.server = server;
        this.sender = new CommandSender(this);
    }

    @Override
    public void run()
    {
        Fantasia.getServerLogger().info(socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " connected.");

        try
        {
            synchronized (socket)
            {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
        }
        catch (Throwable ex)
        {
            Fantasia.getServerLogger().error("FUCK", ex);
            disconnect(true);
            return;
        }

        sendMessage("  ___         _           _\n" +
                " | __|_ _ _ _| |_ __ _ __(_)__ _\n" +
                " | _/ _` | ' \\  _/ _` (_-< / _` |\n" +
                " |_|\\__,_|_||_\\__\\__,_/__/_\\__,_|\n" +
                " --============================--");
        sendMessage(" Welcome to Fantasia, the Supervisor's Telnet console.\n" +
                " This allows you control it even before the Blackbox &\n" +
                " main game have even initialized.");
        sendMessage(" --");
        sendMessage(" Use 'help' for a list of commands.");

        while (isConnected())
        {
            String command;
            try
            {
                command = reader.readLine();
            }
            catch (Exception ex)
            {
                break;
            }

            // No command was sent
            if (command == null)
            {
                break;
            }
            else if (command.isEmpty())
            {
                continue;
            }

            try
            {
                Fantasia.getInstance().getServer().getDispatcher().execute(command, sender);
            }
            catch (CommandSyntaxException ignored)
            {
            }
            catch (Throwable ex)
            {
                Fantasia.getServerLogger().error("An error occurred whilst attempting to execute command " + command, ex);
                sendMessage("Command error: " + ex.getMessage());
            }
        }

        disconnect(false);
    }

    public boolean isConnected()
    {
        synchronized (socket)
        {
            return !socket.isClosed();
        }
    }

    public void disconnect(boolean quiet)
    {
        synchronized (socket)
        {
            if (!quiet)
            {
                sendMessage("Disconnecting...");
            }

            try
            {
                socket.close();
                server.removeSession(this);
            }
            catch (Exception ex)
            {
            }
        }
    }

    public void sendMessage(String message)
    {
        try
        {
            writer.write(message + "\n");
            writer.flush();
        }
        catch (IOException ignored)
        {
        }
    }
}
