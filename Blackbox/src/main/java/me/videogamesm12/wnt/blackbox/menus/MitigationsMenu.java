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

import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;

import javax.swing.*;

public class MitigationsMenu extends JMenu
{
    private final JCheckBoxMenuItem gameRendering = new JCheckBoxMenuItem("Disable rendering completely",
            Supervisor.getConfig().getRenderingSettings().isGameRenderingDisabled());
    private final JCheckBoxMenuItem worldRendering = new JCheckBoxMenuItem("Disable world rendering",
            Supervisor.getConfig().getRenderingSettings().isWorldRenderingDisabled());
    private final JCheckBoxMenuItem weatherRendering = new JCheckBoxMenuItem("Disable weather rendering",
            Supervisor.getConfig().getRenderingSettings().isWeatherRenderingDisabled());
    private final JCheckBoxMenuItem entityRendering = new JCheckBoxMenuItem("Disable entity rendering",
            Supervisor.getConfig().getRenderingSettings().isEntityRenderingDisabled());
    private final JCheckBoxMenuItem tileEntityRendering = new JCheckBoxMenuItem("Disable tile entity rendering",
            Supervisor.getConfig().getRenderingSettings().isTileEntityRenderingDisabled());
    //--
    private final JCheckBoxMenuItem entitySpawning = new JCheckBoxMenuItem("Ignore entity spawning",
            Supervisor.getConfig().getNetworkSettings().isIgnoringEntitySpawns());
    private final JCheckBoxMenuItem explosionSpawning = new JCheckBoxMenuItem("Ignore explosions",
            Supervisor.getConfig().getNetworkSettings().isIgnoringExplosions());
    private final JCheckBoxMenuItem particleSpawning = new JCheckBoxMenuItem("Ignore particle spawns",
            Supervisor.getConfig().getNetworkSettings().isIgnoringParticleSpawns());
    private final JCheckBoxMenuItem lightUpdates = new JCheckBoxMenuItem("Ignore light updates",
            Supervisor.getConfig().getNetworkSettings().isIgnoringLightUpdates());
    //--
    private final JMenuItem disconnect = new JMenuItem("Disconnect");
    private final JMenuItem exit = new JMenuItem("Exit");
    private final JMenuItem exitForcibly = new JMenuItem("Exit (Forcibly)");

    public MitigationsMenu()
    {
        super("Mitigations");

        //==-- RENDER --==//
        final JMenu renderingMenu = new JMenu("Render");
        gameRendering.addActionListener((exd) -> Supervisor.getConfig().getRenderingSettings().setGameRenderingDisabled(gameRendering.isSelected()));
        worldRendering.addActionListener((exd) -> Supervisor.getConfig().getRenderingSettings().setWorldRenderingDisabled(worldRendering.isSelected()));
        weatherRendering.addActionListener((exd) -> Supervisor.getConfig().getRenderingSettings().setWeatherRenderingDisabled(weatherRendering.isSelected()));
        entityRendering.addActionListener((exd) -> Supervisor.getConfig().getRenderingSettings().setEntityRenderingDisabled(entityRendering.isSelected()));
        tileEntityRendering.addActionListener((exd) -> Supervisor.getConfig().getRenderingSettings().setTileEntityRenderingDisabled(tileEntityRendering.isSelected()));

        renderingMenu.add(gameRendering);
        renderingMenu.add(worldRendering);
        renderingMenu.add(weatherRendering);
        renderingMenu.add(entityRendering);
        renderingMenu.add(tileEntityRendering);

        //==-- NETWORK --==//
        final JMenu networkMenu = new JMenu("Network");
        entitySpawning.addActionListener((exd) -> Supervisor.getConfig().getNetworkSettings().setIgnoringEntitySpawns(entitySpawning.isSelected()));
        explosionSpawning.addActionListener((exd) -> Supervisor.getConfig().getNetworkSettings().setIgnoringExplosions(explosionSpawning.isSelected()));
        lightUpdates.addActionListener((exd) -> Supervisor.getConfig().getNetworkSettings().setIgnoringLightUpdates(lightUpdates.isSelected()));
        particleSpawning.addActionListener((exd) -> Supervisor.getConfig().getNetworkSettings().setIgnoringParticleSpawns(particleSpawning.isSelected()));

        networkMenu.add(entitySpawning);
        networkMenu.add(explosionSpawning);
        networkMenu.add((lightUpdates));
        networkMenu.add((particleSpawning));

        //==-- DRASTIC --==//
        final JMenu drasticMenu = new JMenu("Drastic");
        disconnect.addActionListener((exd) -> {
            ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();

            if (handler != null && handler.getConnection() != null)
            {
                handler.getConnection().disconnect(
                        Text.of("Disconnected by Supervisor"));
            }
        });
        exit.addActionListener((exd) -> MinecraftClient.getInstance().scheduleStop());
        exitForcibly.addActionListener((exd) -> System.exit(42069));

        drasticMenu.add(disconnect);
        drasticMenu.add(exit);
        drasticMenu.add(exitForcibly);

        //==-- ENDGAME --==//
        add(renderingMenu);
        add(networkMenu);
        add(new JPopupMenu.Separator());
        add(drasticMenu);
    }
}
