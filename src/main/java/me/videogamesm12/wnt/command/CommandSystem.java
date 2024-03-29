/*
 * Copyright (c) 2022 Video
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

package me.videogamesm12.wnt.command;

import me.videogamesm12.wnt.WNT;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1>CommandSystem</h1>
 * Simple command registration and storage system for WNT
 * @author Video
 */
public class CommandSystem
{
    private static final Map<Class<? extends WCommand>, WCommand> commands = new HashMap<>();

    /**
     * Registers a command class.
     * @param command   Class that extends WCommand
     * @param <T>       WCommand
     */
    public static <T extends WCommand> void registerCommand(Class<T> command)
    {
        try
        {
            // The command has already been registered, so we'll just avoid a disaster waiting to happen.
            if (isRegistered(command))
                throw new IllegalArgumentException("Command class has already been registered!");

            commands.put(command, command.getDeclaredConstructor().newInstance().register());
        }
        catch (Exception ex)
        {
            WNT.getLogger().error("Failed to register command class " + command.getName(), ex);
        }
    }

    /**
     * Checks to see whether or not a command class has been registered.
     * @param command   Class that extends WCommand
     * @param <T>       WCommand
     * @return          True if the command is already registered
     */
    public static <T extends WCommand> boolean isRegistered(Class<T> command)
    {
        return commands.containsKey(command);
    }
}