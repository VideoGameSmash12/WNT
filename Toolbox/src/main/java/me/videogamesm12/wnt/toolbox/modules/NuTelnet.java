package me.videogamesm12.wnt.toolbox.modules;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.toolbox.events.IncomingTelnetMessage;
import me.videogamesm12.wnt.toolbox.events.OutgoingTelnetMessage;
import me.videogamesm12.wnt.toolbox.events.TelnetFailed;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class NuTelnet extends Module implements IncomingTelnetMessage, OutgoingTelnetMessage
{
    private final TelnetClient telnetClient = new TelnetClient();
    private Thread telnetThread = null;

    @Override
    public void onStart()
    {
        IncomingTelnetMessage.EVENT.register(this);
        OutgoingTelnetMessage.EVENT.register(this);
    }

    @Override
    public void onStop()
    {
        if (telnetClient.isConnected())
        {
            disconnect();
        }
    }

    @Override
    public Class<TelnetConfig> getConfigClass()
    {
        return TelnetConfig.class;
    }

    @Override
    public void onTelnetMessage(String text)
    {
        if (MinecraftClient.getInstance().player == null)
            return;

        MinecraftClient.getInstance().player.sendMessage(new TranslatableText("wnt.messages.telnet.prefix", new LiteralText(text).formatted(Formatting.WHITE)).formatted(Formatting.GREEN), false);
    }

    @Override
    public void onTelnetSent(String text)
    {
        if (MinecraftClient.getInstance().player == null)
            return;

        MinecraftClient.getInstance().player.sendMessage(new TranslatableText("wnt.messages.telnet.sent"), false);
    }

    /**
     * Attempts to connect to the currently saved Telnet server.
     */
    public void connect()
    {
        connect(((TelnetConfig) getConfig()).getAddress(), ((TelnetConfig) getConfig()).getPort());
    }

    /**
     * Attempts to connect to a provided Telnet server.
     * @throws IllegalArgumentException If the port is invalid
     * @throws IllegalStateException    If the client is already connected to a server
     * @param ip    String
     * @param port  int
     */
    public void connect(String ip, final int port)
    {
        if (telnetClient.isConnected())
            throw new IllegalStateException("wnt.messages.telnet.already_connected");

        if (Math.abs(port) > 65535)
            throw new IllegalArgumentException("wnt.messages.telnet.invalid_port");

        telnetThread = new Thread(() -> {
            try
            {
                telnetClient.connect(ip, port);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(telnetClient.getInputStream(), StandardCharsets.UTF_8)))
                {
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        IncomingTelnetMessage.EVENT.invoker().onTelnetMessage(line);
                    }
                }

                disconnect();
            }
            catch (IOException ex)
            {
                TelnetFailed.EVENT.invoker().onConnectionFailure(ex);
            }

            telnetThread = null;
        });

        telnetThread.start();
    }

    /**
     * Disconnects the Telnet client from a server. This only does anything if the client is actually connected to a
     *  server in the first place.
     */
    public void disconnect()
    {
        if (telnetClient.isConnected())
        {
            try
            {
                telnetClient.disconnect();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Send a command to the currently connected Telnet server.
     * @param command   String
     */
    public void sendCommand(String command)
    {
        try
        {
            telnetClient.getOutputStream().write((command + "\r\n").getBytes(StandardCharsets.UTF_8));
            telnetClient.getOutputStream().flush();
            //--
            OutgoingTelnetMessage.EVENT.invoker().onTelnetSent(command);
        }
        catch (IOException ignored)
        {
        }
    }

    @Config(name = "wnt-toolbox-telnet")
    public static class TelnetConfig extends MConfig
    {
        @Getter
        @Setter
        private boolean enabled = true;

        @Getter
        @Setter
        private String address;

        @ConfigEntry.BoundedDiscrete(max = 65535, min = 0)
        @Getter
        @Setter
        private int port;
    }
}
