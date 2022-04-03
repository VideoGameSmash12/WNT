package me.videogamesm12.wnt.toolbox.modules;

import me.videogamesm12.wnt.module.WModule;
import me.videogamesm12.wnt.toolbox.events.IncomingTelnetMessage;
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

public class Telnet implements WModule, IncomingTelnetMessage
{
    private final TelnetClient telnetClient = new TelnetClient();
    private Thread telnetThread = null;

    @Override
    public void onInitialize()
    {
        IncomingTelnetMessage.EVENT.register(this);
    }

    @Override
    public void onStop()
    {
        disconnect();
    }

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

    public boolean isConnected()
    {
        return telnetClient.isConnected();
    }

    public void sendCommand(String command)
    {
        try
        {
            telnetClient.getOutputStream().write((command + "\r\n").getBytes(StandardCharsets.UTF_8));
            telnetClient.getOutputStream().flush();
        }
        catch (IOException ignored)
        {
        }
    }

    @Override
    public void onTelnetMessage(String text)
    {
        if (MinecraftClient.getInstance().player == null)
            return;

        MinecraftClient.getInstance().player.sendMessage(new TranslatableText("wnt.messages.telnet.prefix", new LiteralText(text).formatted(Formatting.WHITE)).formatted(Formatting.GREEN), false);
    }

    public static record EnhancedPlusData(String tps)
    {
    }
}
