package me.videogamesm12.w95.toolbox.commands;

import com.google.gson.Gson;
import com.mojang.brigadier.context.CommandContext;
import me.videogamesm12.w95.command.WCommand;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class UuidCommand extends WCommand
{
    private Gson gson               = new Gson();
    private Pattern usernamePattern = Pattern.compile("[A-z0-9_]{3,20}");

    public UuidCommand()
    {
        super("uuid", "", "/uuid <player>");
    }

    @Override
    public boolean run(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        CompletableFuture<Void> retrieval = CompletableFuture.runAsync(() ->
        {
            String username = args[0];

            context.getSource().sendFeedback(new TranslatableText("w95.messages.command.uuid.connect")
                    .formatted(Formatting.GREEN));

            try
            {
                // If the username doesn't match the regex, don't even bother
                if (!usernamePattern.matcher(username).matches())
                    throw new IllegalArgumentException();

                // Sends the request to Mojang's servers
                URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Result result = gson.fromJson(new InputStreamReader(connection.getInputStream()), Result.class);
                context.getSource().sendFeedback(new TranslatableText("w95.messages.command.uuid.result", result.username,
                        new LiteralText(result.uuid).setStyle(Style.EMPTY.withClickEvent(
                                new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, result.uuid)).withUnderline(true)
                                .withColor(TextColor.fromFormatting(Formatting.WHITE))))
                        .formatted(Formatting.GRAY));
            }
            // Player was not found
            catch (FileNotFoundException error)
            {
                context.getSource().sendError(new TranslatableText("w95.messages.commands.player_not_found"));
            }
            // Player's name isn't valid
            catch (IllegalArgumentException | MalformedURLException error)
            {
                context.getSource().sendError(new TranslatableText("w95.messages.commands.invalid_player", username));
            }
            // Some other shit happened
            catch (IOException error)
            {
                context.getSource().sendError(new TranslatableText("w95.messages.commands.failed_to_connect"));
                error.printStackTrace();
            }
        });

        return true;
    }

    @Override
    public List<String> suggest(CommandContext<FabricClientCommandSource> context, String[] args)
    {
        return new ArrayList<>();
    }

    private static class Result
    {
        private String username;

        private String uuid;
    }
}
