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

package me.videogamesm12.wnt.sausage.menu;

import me.videogamesm12.wnt.blackbox.menus.ModMenu;
import me.videogamesm12.wnt.sausage.event.HackToggled;
import net.wurstclient.Category;
import net.wurstclient.WurstClient;

import java.util.Arrays;

public class WurstMenu extends ModMenu<WurstClient> implements HackToggled
{
    public WurstMenu()
    {
        super("Wurst", WurstClient.class);

        // Sets up event listeners
        HackToggled.EVENT.register(this);

        // Adds hacks
        Arrays.stream(Category.values()).forEach(category -> add(new CategoryMenu(category)));
    }

    @Override
    public WurstClient getModInstance()
    {
        return WurstClient.INSTANCE;
    }

    public void refresh()
    {
        Arrays.stream(getMenuComponents()).forEach((comp) -> {
            if (comp instanceof CategoryMenu ccc)
                ccc.refresh();
        });
    }

    @Override
    public void onHackToggled()
    {
        refresh();
    }
}
