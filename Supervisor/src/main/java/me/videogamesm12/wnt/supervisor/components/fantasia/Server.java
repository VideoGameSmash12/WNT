package me.videogamesm12.wnt.supervisor.components.fantasia;

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import lombok.Getter;
import me.videogamesm12.wnt.supervisor.FantasiaSupervisor;
import me.videogamesm12.wnt.supervisor.api.event.ClientFreezeEvent;
import me.videogamesm12.wnt.supervisor.components.fantasia.command.*;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.Session;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Server extends Thread
{
    private List<Session> sessions = new ArrayList<>();
    //--
    @Getter
    private SocketListener socketListener;
    //--
    @Getter
    private final CommandDispatcher<CommandSender> dispatcher = new CommandDispatcher<>();
    @Getter
    private final Map<String, FCommand> commands = new HashMap<>();

    @Override
    public void run()
    {
        Fantasia.getServerLogger().info("Starting up listeners...");
        FantasiaSupervisor.getEventBus().register(this);
        try
        {
            socketListener = new SocketListener(this, new ServerSocket(6969, 999));
            socketListener.start();
        }
        catch (Throwable ex)
        {
            Fantasia.getServerLogger().error("WTF?", ex);
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

    public void addSession(Session session)
    {
        sessions.add(session);
    }

    public void removeSession(Session session)
    {
        sessions.remove(session);
    }

    public void shutdown()
    {
        try
        {
            socketListener.getSocket().close();
        }
        catch (Exception ignored)
        {
        }

        for (Session session : sessions)
        {
            CompletableFuture.runAsync(() -> session.disconnect(false));
        }

        sessions.clear();
        interrupt();
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
        broadcast(" ** CLIENT FREEZE DETECTED, LAST RENDERED " + event.getLastRendered() + " MS AGO ** ");
    }

    public static class SocketListener extends Thread
    {
        private final Server server;
        @Getter
        private final ServerSocket socket;

        public SocketListener(Server server, ServerSocket socket)
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

                Session session = new Session(server, clientSocket);
                server.addSession(session);
                session.start();
            }
        }
    }
}
