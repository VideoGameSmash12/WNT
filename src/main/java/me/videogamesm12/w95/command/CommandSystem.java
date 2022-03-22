package me.videogamesm12.w95.command;

import java.util.HashMap;
import java.util.Map;

public class CommandSystem
{
    private static final Map<Class<? extends WCommand>, WCommand> commands = new HashMap<>();

    public static <T extends WCommand> void registerCommand(Class<T> command)
    {
        try
        {
            commands.put(command, command.getDeclaredConstructor().newInstance().register());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static <T extends WCommand> boolean isRegistered(Class<T> command)
    {
        return commands.containsKey(command);
    }
}