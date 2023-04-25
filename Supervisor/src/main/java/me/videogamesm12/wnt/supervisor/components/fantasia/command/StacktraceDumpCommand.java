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

package me.videogamesm12.wnt.supervisor.components.fantasia.command;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.fantasia.session.CommandSender;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

public class StacktraceDumpCommand extends FCommand
{
    public StacktraceDumpCommand()
    {
        super("stacktracedump", "Dumps all stacktraces from all threads", "/stacktracedump");
    }

    @Override
    public boolean run(CommandSender sender, CommandContext<CommandSender> context, String[] args)
    {
        Arrays.stream(ManagementFactory.getThreadMXBean().dumpAllThreads(true, true)).forEach(thread ->
        {
            String header = "-- == ++ STACKTRACE DUMP - " + thread.getThreadName() + " ++ == --";
            String status = "STATUS: " + thread.getThreadState().name();
            String details = "DETAILS: "+ String.format("Suspended: %s, Native: %s", thread.isSuspended() ? "Yes" : "No", thread.isInNative() ? "Yes" : "No");
            List<String> stacktrace = Arrays.stream(thread.getStackTrace()).map(StackTraceElement::toString).toList();

            Fantasia.getServerLogger().info(header);
            Fantasia.getServerLogger().info(status);
            Fantasia.getServerLogger().info(details);
            stacktrace.forEach(trace -> Fantasia.getServerLogger().info("    " + trace));

            sender.sendMessage(header);
            sender.sendMessage(status);
            sender.sendMessage(details);
            stacktrace.forEach(trace -> sender.sendMessage("    " + trace));
        });

        return true;
    }
}
