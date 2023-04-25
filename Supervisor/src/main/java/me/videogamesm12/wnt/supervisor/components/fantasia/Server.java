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

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import lombok.Getter;
import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.api.event.ClientFreezeEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.command.*;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.event.SessionStartedPreSetupEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.listener.IConnectionListener;
import me.videogamesm12.wnt.supervisor.components.fantasia.listener.TelnetConnectionListener;
import me.videogamesm12.wnt.supervisor.components.fantasia.listener.UnixDomainConnectionListener;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.ISession;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Server extends Thread
{
    private final List<ISession> sessions = new ArrayList<>();
    //--
    @Getter
    private IConnectionListener connectionListener;
    //--
    @Getter
    private final CommandDispatcher<CommandSender> dispatcher = new CommandDispatcher<>();
    @Getter
    private final Map<String, FCommand> commands = new HashMap<>();

    public Server()
    {
        super("Fantasia-Server");
    }

    @Override
    public void run()
    {
        Fantasia.getServerLogger().info("Starting up listeners...");
        Supervisor.getEventBus().register(this);
        try
        {
            connectionListener = switch (Supervisor.getConfig().getFantasiaSettings().getConnectionType())
            {
                case TELNET -> new TelnetConnectionListener(this, new ServerSocket(Supervisor.getConfig().getFantasiaSettings().getPort(), 999));
                case UNIX -> new UnixDomainConnectionListener(this);
            };
            connectionListener.start();
        }
        catch (Throwable ex)
        {
            Fantasia.getServerLogger().error("Failed to start Fantasia server", ex);
            return;
        }

        Fantasia.getServerLogger().info("Registering commands...");;
        registerCommand(CrashCommand.class);
        registerCommand(ChatCommand.class);
        registerCommand(DisconnectCommand.class);
        registerCommand(ExitCommand.class);
        registerCommand(HelpCommand.class);
        registerCommand(RunCommand.class);
        registerCommand(ShutdownCommand.class);
        registerCommand(StacktraceDumpCommand.class);
        Fantasia.getServerLogger().info("Commands registered");
    }

    @Override
    public void interrupt()
    {
        shutdown();
        super.interrupt();
    }

    public void addSession(ISession session)
    {
        sessions.add(session);
    }

    public void removeSession(ISession session)
    {
        sessions.remove(session);
    }

    public void shutdown()
    {
        try
        {
            connectionListener.shutdown();
        }
        catch (Exception ignored)
        {
        }

        for (ISession session : sessions)
        {
            CompletableFuture.runAsync(() -> session.disconnect(false));
        }

        sessions.clear();
    }

    private void registerCommand(Class<? extends FCommand> cmd)
    {
        try
        {
            FCommand command = cmd.getDeclaredConstructor().newInstance();
            commands.put(command.getName(), command);
            dispatcher.register(CommandSender.literal(command.getName()).then(CommandSender.argument("args",
                    StringArgumentType.greedyString()).executes(command)).executes(command));
        }
        catch (Exception ex)
        {
            Fantasia.getServerLogger().warn("Failed to register command " + cmd.getName(), ex);
        }
    }

    public void broadcast(String message)
    {
        sessions.forEach(session -> CompletableFuture.runAsync(() -> session.sendMessage(message)));
    }

    @Subscribe
    public void onClientFreeze(ClientFreezeEvent event)
    {
        broadcast(" ** WARNING: CLIENT FREEZE DETECTED, LAST RENDERED " + event.getLastRendered() + " MS AGO ** ");
    }

    @Subscribe
    public void onSessionStartedPreSetup(SessionStartedPreSetupEvent event)
    {
        Fantasia.getServerLogger().info(event.getSession().getConnectionIdentifier() + " connected.");
    }

    @Subscribe
    public void onSessionStarted(SessionStartedEvent event)
    {
        ISession session = event.getSession();
        session.sendMessage("""
                  ___         _           _
                 | __|_ _ _ _| |_ __ _ __(_)__ _
                 | _/ _` | ' \\  _/ _` (_-< / _` |
                 |_|\\__,_|_||_\\__\\__,_/__/_\\__,_|
                 --============================--\
                """);
        session.sendMessage("""
                 Welcome to Fantasia, the Supervisor's internal console.
                 This allows you control it even before the Blackbox &
                 main game have even initialized.\
                """);
        session.sendMessage(" --");
        session.sendMessage(" Use 'help' for a list of commands.");
    }
}
