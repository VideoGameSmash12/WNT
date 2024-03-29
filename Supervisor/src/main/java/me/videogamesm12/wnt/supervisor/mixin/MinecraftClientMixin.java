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

package me.videogamesm12.wnt.supervisor.mixin;

import me.videogamesm12.wnt.supervisor.Supervisor;
import me.videogamesm12.wnt.supervisor.components.fantasia.Fantasia;
import me.videogamesm12.wnt.supervisor.components.flags.Flags;
import me.videogamesm12.wnt.supervisor.components.watchdog.Watchdog;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

/**
 * <h1>MinecraftClientMixin</h1>
 * <p>The Supervisor uses this as a point of reference to determine if a client has hanged.</p>
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    /**
     * <p>Supervisor's freeze detection works by injecting some code at the tail-end of the game's rendering method to
     *  store a timestamp for when the last time a frame successfully rendered occurs, then periodically checking
     *  through another thread if it exceeds 5 seconds.</p>
     * <p>This code is what stores the timestamps.</p>
     * @param bool  boolean
     * @param ci    CallbackInfo
     */
    @Inject(method = "render", at = @At("RETURN"))
    public void onPostRender(boolean bool, CallbackInfo ci)
    {
        if (Supervisor.getConfig().getWatchdogSettings().isFreezeDetectionEnabled())
        {
            Watchdog.LAST_RENDERED_TIME = Instant.now().toEpochMilli();
        }
    }

    /**
     * <p>This forces the Supervisor to properly shut down after the client has crashed if a mod like Not Enough Crashes is not present.</p>
     * <p>If the crash was intentionally caused by the Supervisor, this reverts also the flag if Not Enough Crashes was detected to avoid a potential softlock.</p>
     * @param ci    CallbackInfo
     */
    @Inject(method = "cleanUpAfterCrash", at = @At("RETURN"))
    public void onCleanUpAfterCrash(CallbackInfo ci)
    {
        if (!FabricLoader.getInstance().isModLoaded("notenoughcrashes"))
        {
            Supervisor.getInstance().shutdown();
        }
        else
        {
            Flags flags = Supervisor.getInstance().getFlags();

            if (flags.isSupposedToCrash())
            {
                flags.setSupposedToCrash(false);
            }
        }
    }

    /**
     * <p>This will intentionally crash the client if the relevant flags are set.</p>
     * @param ci    CallbackInfo
     */
    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;endMonitor(ZLnet/minecraft/util/TickDurationMonitor;)V", shift = At.Shift.AFTER))
    public void intentionallyCrash(CallbackInfo ci)
    {
        if (Supervisor.getInstance().getFlags().isSupposedToCrash())
        {
            Fantasia.getServerLogger().info("Hey, want to see a magic trick?");
            int lol = 0 / 0;
        }
    }
}
