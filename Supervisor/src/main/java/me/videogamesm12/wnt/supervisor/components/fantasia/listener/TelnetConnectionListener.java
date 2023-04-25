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
        super("Fantasia-TelnetConnectionListener");
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
