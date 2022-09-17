package me.videogamesm12.wnt.toolbox.util;

import com.google.gson.Gson;
import lombok.Getter;
import me.videogamesm12.wnt.toolbox.commands.NameCommand;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class AshconUtil
{
    private static Gson gson = new Gson();

    public static AshconResponse getAshconData(String nameOrUuid) throws IOException
    {
        // Sends the request to Mojang's servers
        URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + nameOrUuid);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        return gson.fromJson(new InputStreamReader(connection.getInputStream()), AshconResponse.class);
    }

    @Getter
    public static class AshconResponse
    {
        private String username;

        private String uuid;
    }
}
