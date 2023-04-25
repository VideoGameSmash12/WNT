package me.videogamesm12.wnt.supervisor.components.fantasia.session;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.Server;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedPreSetupEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.Instant;

public class UnixDomainSession extends Thread implements ISession
{
    private final Server server;
    private final SocketChannel socket;
    private final CommandSender sender;
    private final String idenitifier;

    public UnixDomainSession(Server server, SocketChannel socket)
    {
        this.server = server;
        this.socket = socket;
        this.sender = new CommandSender(this);
        this.idenitifier = String.valueOf(Instant.now().toEpochMilli());
    }

    @Override
    public void run()
    {
        Supervisor.getEventBus().post(new SessionStartedPreSetupEvent(this));
        Supervisor.getEventBus().post(new SessionStartedEvent(this));

        while (isConnected())
        {
            String command;
            try
            {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int bytesRead = socket.read(buffer);

                if (bytesRead < 0)
                {
                    command = "";
                }
                else
                {
                    byte[] bytes = new byte[bytesRead];
                    buffer.flip();
                    buffer.get(bytes);
                    command = new String(bytes).replaceAll("\n$", "");
                }
            }
            catch (Exception ex)
            {
                break;
            }

            if (command.isEmpty())
            {
                continue;
            }

            try
            {
                Fantasia.getServerLogger().info(sender.getSession().getConnectionIdentifier() + " issued client command '" + command + "'");
                Fantasia.getInstance().getServer().getDispatcher().execute(command, sender);
            }
            catch (CommandSyntaxException ignored)
            {
                sendMessage("Unknown command: " + command.split(" ")[0]);
            }
            catch (Throwable ex)
            {
                Fantasia.getServerLogger().error("An error occurred whilst attempting to execute command " + command, ex);
                sendMessage("Command error: " + ex.getMessage());
            }
        }
    }

    @Override
    public String getConnectionIdentifier()
    {
        return idenitifier;
    }

    @Override
    public boolean isConnected()
    {
        return socket.isConnected();
    }

    @Override
    public void disconnect(boolean quiet)
    {
        if (!quiet)
        {
            sendMessage("Disconnecting...");
        }

        try
        {
            socket.close();
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    public void sendMessage(String message)
    {
        // Hack that adds a new line after the message
        message = message + "\n";
        //--
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();

        while (buffer.hasRemaining())
        {
            try
            {
                socket.write(buffer);
            }
            catch (IOException ex)
            {
                // DEBUG
                ex.printStackTrace();
                return;
            }
        }
    }
}
