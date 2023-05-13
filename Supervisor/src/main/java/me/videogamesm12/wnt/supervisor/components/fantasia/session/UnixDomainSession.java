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

package me.videogamesm12.wnt.supervisor.components.fantasia.session;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.Server;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionPreProcessCommandEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedPreSetupEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.Instant;

/**
 * <h1>UnixDomainSession</h1>
 * <p>An implementation of ISession for Unix domain socket connections.</p>
 */
public class UnixDomainSession extends Thread implements ISession
{
    private final Server server;
    private final SocketChannel socket;
    private final CommandSender sender;
    private final String identifier;

    public UnixDomainSession(Server server, SocketChannel socket)
    {
        this.server = server;
        this.socket = socket;
        this.sender = new CommandSender(this);
        this.identifier = String.valueOf(Instant.now().toEpochMilli());
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
                final SessionPreProcessCommandEvent event = new SessionPreProcessCommandEvent(sender.session(), command);
                Supervisor.getEventBus().post(event);

                if (!event.isCancelled())
                {
                    Fantasia.getInstance().getServer().getDispatcher().execute(command, sender);
                }
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

    /**
     * Returns the timestamp of when the session was established, which is used as an identifier.
     * @return  String
     */
    @Override
    public String getConnectionIdentifier()
    {
        return identifier;
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
            server.removeSession(this);
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
