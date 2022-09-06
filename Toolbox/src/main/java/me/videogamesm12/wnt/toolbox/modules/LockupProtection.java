/*
 * Copyright (c) 2022 Video
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

package me.videogamesm12.wnt.toolbox.modules;

import com.google.common.eventbus.Subscribe;
import me.videogamesm12.wnt.module.Module;
import me.videogamesm12.wnt.module.ModuleMeta;
import me.videogamesm12.wnt.toolbox.Toolbox;
import me.videogamesm12.wnt.toolbox.event.network.S2COpenScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandlerType;

@ModuleMeta(name = "LockupProtection", description = "Resists the effect of TotalFreedomMod's /lockup command")
public class LockupProtection extends Module
{
    @Override
    public void onStart()
    {
        Toolbox.getEventBus().register(this);
    }

    @Override
    public void onStop()
    {
        Toolbox.getEventBus().unregister(this);
    }

    @Subscribe
    public void onS2COpenScreen(S2COpenScreen event)
    {
        if (!isEnabled() || MinecraftClient.getInstance().player == null)
            return;

        if (event.getPacket().getScreenHandlerType() == ScreenHandlerType.GENERIC_9X4)
        {
            event.setCancelled(true);
        }
    }
}
