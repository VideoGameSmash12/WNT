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
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedPreSetupEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TelnetSession extends Thread implements ISession
{
    private Server server;
    private final Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private CommandSender sender;

    public TelnetSession(Server server, Socket socket)
    {
        this.socket = socket;
        this.server = server;
        this.sender = new CommandSender(this);
    }

    @Override
    public void run()
    {
        Supervisor.getEventBus().post(new SessionStartedPreSetupEvent(this));

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

        // Just in case...
        if (!Supervisor.getConfig().getFantasiaSettings().isNonLocalConnectionsAllowed()
                && !socket.getInetAddress().equals(InetAddress.getLoopbackAddress()))
        {
            try
            {
                sendMessage("This instance of Fantasia doesn't allow connections from non-local IP addresses.");
                disconnect(true);
                return;
            }
            catch (Exception ignored)
            {
            }
        }

        Supervisor.getEventBus().post(new SessionStartedEvent(this));

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

        disconnect(false);
    }

    @Override
    public void interrupt()
    {
        disconnect(true);
        super.interrupt();
    }

    @Override
    public String getConnectionIdentifier()
    {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    @Override
    public boolean isConnected()
    {
        synchronized (socket)
        {
            return !socket.isClosed();
        }
    }

    @Override
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
            catch (Exception ignored)
            {
            }
        }
    }

    @Override
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
