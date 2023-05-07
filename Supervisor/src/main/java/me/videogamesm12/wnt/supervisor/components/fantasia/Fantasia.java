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
    private static final Logger serverLogger = LoggerFactory.getLogger("Fantasia-Server");
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
