package me.videogamesm12.wnt.overhauled_blackbox;

import me.videogamesm12.wnt.WNT;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Util;

public class Entrypoints implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        switch (Util.getOperatingSystem())
        {
            case SOLARIS, UNKNOWN ->
            {
                WNT.getLogger().warn("The Blackbox has not been properly tested under this operating system, so in the "
                        + "interest of maintaining client stability, it has been disabled.");
                return;
            }
            case LINUX, OSX ->
            {
                // https://bugs.openjdk.org/browse/JDK-8056151
                System.setProperty("sun.java2d.xrender", "f");
            }
        }

        Blackbox.setup();
    }
}
