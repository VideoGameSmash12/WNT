package me.videogamesm12.wnt.blackbox.menus;

import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;

import javax.swing.*;

public class MitigationsMenu extends JMenu
{
    private final JMenu renderingMenu = new JMenu("Render");
    private final JCheckBoxMenuItem gameRendering = new JCheckBoxMenuItem("Disable rendering completely", Supervisor.CONFIG.rendering().disableGameRendering());
    private final JCheckBoxMenuItem worldRendering = new JCheckBoxMenuItem("Disable world rendering", Supervisor.CONFIG.rendering().disableWorldRendering());
    private final JCheckBoxMenuItem weatherRendering = new JCheckBoxMenuItem("Disable weather rendering", Supervisor.CONFIG.rendering().disableWeatherRendering());
    private final JCheckBoxMenuItem entityRendering = new JCheckBoxMenuItem("Disable entity rendering", Supervisor.CONFIG.rendering().disableEntityRendering());
    private final JCheckBoxMenuItem tileEntityRendering = new JCheckBoxMenuItem("Disable tile entity rendering", Supervisor.CONFIG.rendering().disableTileEntityRendering());
    //--
    private final JMenu networkMenu = new JMenu("Network");
    private final JCheckBoxMenuItem entitySpawning = new JCheckBoxMenuItem("Ignore entity spawning", Supervisor.CONFIG.network().ignoreEntitySpawns());
    private final JCheckBoxMenuItem explosionSpawning = new JCheckBoxMenuItem("Ignore explosions", Supervisor.CONFIG.network().ignoreExplosions());
    private final JCheckBoxMenuItem particleSpawning = new JCheckBoxMenuItem("Ignore particle spawns", Supervisor.CONFIG.network().ignoreParticleSpawns());
    private final JCheckBoxMenuItem lightUpdates = new JCheckBoxMenuItem("Ignore light updates", Supervisor.CONFIG.network().ignoreLightUpdates());
    //--
    private final JMenu drasticMenu = new JMenu("Drastic");
    private final JMenuItem disconnect = new JMenuItem("Disconnect");
    private final JMenuItem exit = new JMenuItem("Exit");
    private final JMenuItem exitForcibly = new JMenuItem("Exit (Forcibly)");

    public MitigationsMenu()
    {
        super("Mitigations");

        //==-- RENDER --==//
        gameRendering.addActionListener((exd) -> Supervisor.CONFIG.rendering().setDisableGameRendering(gameRendering.isSelected()));
        worldRendering.addActionListener((exd) -> Supervisor.CONFIG.rendering().setDisableWorldRendering(worldRendering.isSelected()));
        weatherRendering.addActionListener((exd) -> Supervisor.CONFIG.rendering().setDisableWeatherRendering(weatherRendering.isSelected()));
        entityRendering.addActionListener((exd) -> Supervisor.CONFIG.rendering().setDisableEntityRendering(entityRendering.isSelected()));
        tileEntityRendering.addActionListener((exd) -> Supervisor.CONFIG.rendering().setDisableTileEntityRendering(tileEntityRendering.isSelected()));

        renderingMenu.add(gameRendering);
        renderingMenu.add(worldRendering);
        renderingMenu.add(weatherRendering);
        renderingMenu.add(entityRendering);
        renderingMenu.add(tileEntityRendering);

        //==-- NETWORK --==//
        entitySpawning.addActionListener((exd) -> Supervisor.CONFIG.network().setIgnoreEntitySpawns(entitySpawning.isSelected()));
        explosionSpawning.addActionListener((exd) -> Supervisor.CONFIG.network().setIgnoreExplosionSpawns(explosionSpawning.isSelected()));
        lightUpdates.addActionListener((exd) -> Supervisor.CONFIG.network().setIgnoreLightUpdates(lightUpdates.isSelected()));
        particleSpawning.addActionListener((exd) -> Supervisor.CONFIG.network().setIgnoreParticleSpawns(particleSpawning.isSelected()));

        networkMenu.add(entitySpawning);
        networkMenu.add(explosionSpawning);
        networkMenu.add((lightUpdates));
        networkMenu.add((particleSpawning));

        //==-- DRASTIC --==//
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
