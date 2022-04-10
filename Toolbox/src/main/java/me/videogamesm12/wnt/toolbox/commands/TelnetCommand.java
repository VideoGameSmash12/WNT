package me.videogamesm12.wnt.toolbox.commands;

import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.command.WCommand;
import me.videogamesm12.wnt.toolbox.modules.NuTelnet;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelnetCommand extends WCommand
{
    public TelnetCommand()
    {
        super("telnet", "Woah, technology!", "/telnet <connect <ip> <port> | disconnect | send <command>>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        NuTelnet telnet = WNT.MODULES.getModule(NuTelnet.class);

        if (args.length == 0)
        {
            return false;
        }

        switch (args[0].toLowerCase())
        {
            case "connect" -> {
                if (args.length < 3)
                    return false;

                String ip = args[1].toLowerCase();
                int port = Integer.parseInt(args[2]);

                telnet.connect(ip, port);
            }
            case "disconnect" -> {
                telnet.disconnect();
            }
            case "send" -> {
                if (args.length < 2)
                    return false;

                telnet.sendCommand(StringUtils.join(args, " ", 1, args.length));
            }
            default -> {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length < 2)
        {
            return Arrays.asList("connect", "disconnect", "send");
        }

        return new ArrayList<>();
    }
}
