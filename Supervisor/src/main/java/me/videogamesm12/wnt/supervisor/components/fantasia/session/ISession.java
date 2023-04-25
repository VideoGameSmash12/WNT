package me.videogamesm12.wnt.supervisor.components.fantasia.session;

public interface ISession
{
    String getConnectionIdentifier();

    boolean isConnected();

    void disconnect(boolean quiet);

    void sendMessage(String message);
}
