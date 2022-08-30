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

import lombok.Getter;

public abstract class Module
{
    @Getter
    private ModuleMeta meta;

    public Module()
    {
        if (!getClass().isAnnotationPresent(ModuleMeta.class))
            throw new IllegalArgumentException("Modules are required to have metadata attached to it");
        else
            meta = getClass().getAnnotation(ModuleMeta.class);
    }

    @Getter
    private boolean enabled;
    @Getter
    private boolean started;

    public final void enable()
    {
        enabled = true;
        onEnable();
    }

    public final void disable()
    {
        enabled = false;
        onDisable();
    }

    public final void start()
    {
        onStart();
        started = true;
    }

    public final void stop()
    {
        onStop();
        started = false;
    }

    public void onDisable()
    {
    }

    public void onEnable()
    {
    }

    public abstract void onStart();

    public abstract void onStop();
}