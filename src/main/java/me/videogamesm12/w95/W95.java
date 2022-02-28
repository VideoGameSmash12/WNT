package me.videogamesm12.w95;

import me.videogamesm12.w95.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class W95 implements ModInitializer
{
    public static Logger LOGGER = LogManager.getLogger("W95");
    public static ModuleManager MODULES;

    @Override
    public void onInitialize()
    {
        MODULES = new ModuleManager();
    }

    public static File getW95Folder()
    {
        File file = new File(MinecraftClient.getInstance().runDirectory, "w95");

        if (!file.isDirectory())
        {
            file.mkdirs();
        }

        return file;
    }
}
