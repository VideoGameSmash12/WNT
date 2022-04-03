package me.videogamesm12.wnt;

import me.videogamesm12.wnt.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class WNT implements ModInitializer
{
    public static Logger LOGGER = LogManager.getLogger("WNT");
    public static ModuleManager MODULES;

    @Override
    public void onInitialize()
    {
        MODULES = new ModuleManager();
    }

    @Deprecated
    public static File getW95Folder()
    {
        return getWNTFolder();
    }

    public static File getWNTFolder()
    {
        File file = new File(MinecraftClient.getInstance().runDirectory, "wnt");

        if (!file.isDirectory())
        {
            file.mkdirs();
        }

        return file;
    }
}
