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

package me.videogamesm12.wnt.satellite.menu;

import me.videogamesm12.wnt.blackbox.menus.ModMenu;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.meteor.ActiveModulesChangedEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

public class MeteorMenu extends ModMenu<MeteorClient>
{
    public MeteorMenu()
    {
        super("Meteor", MeteorClient.class);

        // Sets up event listeners
        MeteorClient.EVENT_BUS.registerLambdaFactory("me.videogamesm12.wnt.satellite.menu", (lim, clazz) -> (MethodHandles.Lookup) lim.invoke(null, clazz, MethodHandles.lookup()));
        MeteorClient.EVENT_BUS.subscribe(this);

        Modules.loopCategories().forEach(category -> add(new CategoryMenu(category)));
    }

    @Override
    public MeteorClient getModInstance()
    {
        return MeteorClient.INSTANCE;
    }

    public void refresh()
    {
        Arrays.stream(getMenuComponents()).forEach((comp) -> {
            if (comp instanceof CategoryMenu ccc)
                ccc.refresh();
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStateChanged(ActiveModulesChangedEvent event)
    {
        refresh();
    }
}
