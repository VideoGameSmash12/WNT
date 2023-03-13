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

import lombok.Getter;
import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.Blackbox;
import me.videogamesm12.wnt.dumper.events.RequestEntityDumpEvent;
import me.videogamesm12.wnt.dumper.events.RequestMapDumpEvent;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import javax.swing.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WNTMenu extends JMenu
{
    @Getter
    private static final Queue<ModMenu<?>> queue = new ConcurrentLinkedQueue<>();
    //--
    private ScheduledExecutorService hookManager = new ScheduledThreadPoolExecutor(1);

    private final JMenu hooksMenu = new JMenu("Hooks");

    public WNTMenu()
    {
        super("WNT");

        hookManager.scheduleAtFixedRate(() -> {
            for (int i = 0; i < queue.size(); i++)
                addHook(queue.poll());
        }, 0, 1000, TimeUnit.MILLISECONDS);

        add(new ModulesMenu());
        add(new DumperMenu());
        add(hooksMenu);
    }

    public <V, T extends ModMenu<V>> void addHook(T hook)
    {
        hooksMenu.add(hook);
    }

    public static class DumperMenu extends JMenu
    {
        public DumperMenu()
        {
            super("Dump");

            // --== Entities ==-- //
            final JMenu entitiesMenu = new JMenu("Entities");
            JMenuItem allEntities = new JMenuItem("Dump all entities in memory to disk");
            allEntities.addActionListener((e) ->
            {
                if (MinecraftClient.getInstance().world == null)
                {
                    JOptionPane.showMessageDialog(this, "You're not in a world.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                WNT.getEventBus().post(new RequestEntityDumpEvent(Blackbox.IDENTIFIER));
            });
            entitiesMenu.add(allEntities);
            //--
            JMenuItem specificEntity = new JMenuItem("Dump specific entity in memory to disk");
            specificEntity.addActionListener((e) ->
            {
                if (MinecraftClient.getInstance().world == null)
                {
                    JOptionPane.showMessageDialog(this, "You're not in a world.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = JOptionPane.showInputDialog(this, "Enter the ID of the entity you would like to grab.", "Input Required", JOptionPane.INFORMATION_MESSAGE);
                try
                {
                    int conv = Integer.parseInt(id);
                    Entity entity = MinecraftClient.getInstance().world.getEntityById(conv);

                    if (entity == null)
                    {
                        JOptionPane.showMessageDialog(this, "That entity isn't in memory.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    WNT.getEventBus().post(new RequestEntityDumpEvent(Blackbox.IDENTIFIER, entity));
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this, "You need to input a number.", "Bruh", JOptionPane.ERROR_MESSAGE);
                }
            });
            entitiesMenu.add(specificEntity);

            // --== Maps ==-- //
            final JMenu mapsMenu = new JMenu("Maps");
            //--
            JMenuItem allMaps = new JMenuItem("Dump all map data in memory to disk");
            allMaps.addActionListener((e) ->
            {
                if (MinecraftClient.getInstance().world == null)
                {
                    JOptionPane.showMessageDialog(this, "You're not in a world.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                WNT.getEventBus().post(new RequestMapDumpEvent(Blackbox.IDENTIFIER));
            });
            mapsMenu.add(allMaps);
            //--
            JMenuItem specificMap = new JMenuItem("Dump specific map in memory to disk");
            specificMap.addActionListener((e) ->
            {
                if (MinecraftClient.getInstance().world == null)
                {
                    JOptionPane.showMessageDialog(this, "You're not in a world.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = JOptionPane.showInputDialog(this, "Enter the ID of the map you would like to grab.", "Input Required", JOptionPane.INFORMATION_MESSAGE);
                try
                {
                    int conv = Integer.parseInt(id.replace("#", "").trim());

                    if (!((ClientWorldMixin) MinecraftClient.getInstance().world).getMapStates().containsKey("map_" + id))
                    {
                        JOptionPane.showMessageDialog(this, "That map isn't in memory.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    WNT.getEventBus().post(new RequestMapDumpEvent(Blackbox.IDENTIFIER, conv));
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this, "You need to input a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            mapsMenu.add(specificMap);

            add(entitiesMenu);
            add(mapsMenu);
        }
    }
}
