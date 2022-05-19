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

package me.videogamesm12.wnt.module;

import lombok.Data;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public abstract class Module
{
    public MConfig config;

    public Module()
    {
        AutoConfig.register(getConfigClass(), GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(getConfigClass()).getConfig();
    }

    public final void enable()
    {
        getConfig().setEnabled(true);
        onEnable();
    }

    public final void disable()
    {
        getConfig().setEnabled(false);
        onDisable();
    }

    public final void start()
    {
        onStart();
    }

    public final void stop()
    {
        onStop();
    }

    public void onDisable()
    {
    }

    public void onEnable()
    {
    }

    public abstract void onStart();

    public abstract void onStop();

    public final <T extends MConfig> T getConfig()
    {
        return (T) config;
    }

    public abstract Class<? extends MConfig> getConfigClass();

    public final boolean isEnabled()
    {
        return getConfig().isEnabled();
    }

    @Data
    public static class MConfig implements ConfigData
    {
        private boolean enabled;
    }
}