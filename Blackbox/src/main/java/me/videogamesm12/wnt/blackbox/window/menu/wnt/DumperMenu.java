package me.videogamesm12.wnt.blackbox.window.menu.wnt;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.dumper.events.RequestEntityDumpEvent;
import me.videogamesm12.wnt.dumper.events.RequestMapDumpEvent;
import me.videogamesm12.wnt.dumper.mixin.ClientWorldMixin;
import me.videogamesm12.wnt.blackbox.Blackbox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

import javax.swing.*;

public class DumperMenu extends JMenu
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

            WNT.getEventBus().post(new RequestEntityDumpEvent(Blackbox.getIdentifier()));
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

                WNT.getEventBus().post(new RequestEntityDumpEvent(Blackbox.getIdentifier(), entity));
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

            WNT.getEventBus().post(new RequestMapDumpEvent(Blackbox.getIdentifier()));
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

                WNT.getEventBus().post(new RequestMapDumpEvent(Blackbox.getIdentifier(), conv));
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