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

package me.videogamesm12.wnt.blackbox.menus;

import me.videogamesm12.wnt.WNT;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class WNTMenu extends JMenu
{
    public static Set<Class<? extends ModMenu<?>>> QUEUE = new HashSet<>();

    private final JMenu hooksMenu = new JMenu("Hooks");

    public WNTMenu()
    {
        super("WNT");

        QUEUE.forEach((clazz) -> {
            try
            {
                addHook(clazz.getConstructor().newInstance());
            }
            catch (Exception | Error ex)
            {
                WNT.LOGGER.error("Failed to register queued menu");
                ex.printStackTrace();
            }
        });
        QUEUE.clear();

        add(hooksMenu);
    }

    public <V, T extends ModMenu<V>> void addHook(T hook)
    {
        hooksMenu.add(hook);
    }
}
