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
        super("stacktracedump", "Dumps all stacktraces in memory", "/stacktracedump");
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
