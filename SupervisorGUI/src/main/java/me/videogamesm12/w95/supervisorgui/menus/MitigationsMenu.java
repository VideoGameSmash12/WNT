package me.videogamesm12.w95.supervisorgui.menus;

import me.videogamesm12.w95.supervisor.Supervisor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;

import javax.swing.*;

public class MitigationsMenu extends JMenu
{
    private final JMenu renderingMenu = new JMenu("Render");
    private final JCheckBoxMenuItem gameRendering = new JCheckBoxMenuItem("Disable rendering completely", Supervisor.SupervisorModule.CONFIG.rendering().disableGameRendering());
    private final JCheckBoxMenuItem worldRendering = new JCheckBoxMenuItem("Disable world rendering", Supervisor.SupervisorModule.CONFIG.rendering().disableWorldRendering());
    private final JCheckBoxMenuItem weatherRendering = new JCheckBoxMenuItem("Disable weather rendering", Supervisor.SupervisorModule.CONFIG.rendering().disableWeatherRendering());
    private final JCheckBoxMenuItem entityRendering = new JCheckBoxMenuItem("Disable entity rendering", Supervisor.SupervisorModule.CONFIG.rendering().disableEntityRendering());
    private final JCheckBoxMenuItem tileEntityRendering = new JCheckBoxMenuItem("Disable tile entity rendering", Supervisor.SupervisorModule.CONFIG.rendering().disableTileEntityRendering());
    //--
    private final JMenu networkMenu = new JMenu("Network");
    private final JCheckBoxMenuItem entitySpawning = new JCheckBoxMenuItem("Ignore entity spawning");
    private final JCheckBoxMenuItem explosionSpawning = new JCheckBoxMenuItem("Ignore explosions");
    //--
    private final JMenu drasticMenu = new JMenu("Drastic");
    private final JMenuItem disconnect = new JMenuItem("Disconnect");
    private final JMenuItem exit = new JMenuItem("Exit");
    private final JMenuItem exitForcibly = new JMenuItem("Exit (Forcibly)");

    public MitigationsMenu()
    {
        super("Mitigations");

        //==-- RENDER --==//
        gameRendering.addActionListener((exd) -> Supervisor.SupervisorModule.CONFIG.rendering().setDisableGameRendering(gameRendering.isSelected()));
        worldRendering.addActionListener((exd) -> Supervisor.SupervisorModule.CONFIG.rendering().setDisableWorldRendering(worldRendering.isSelected()));
        weatherRendering.addActionListener((exd) -> Supervisor.SupervisorModule.CONFIG.rendering().setDisableWeatherRendering(weatherRendering.isSelected()));
        entityRendering.addActionListener((exd) -> Supervisor.SupervisorModule.CONFIG.rendering().setDisableEntityRendering(entityRendering.isSelected()));
        tileEntityRendering.addActionListener((exd) -> Supervisor.SupervisorModule.CONFIG.rendering().setDisableTileEntityRendering(tileEntityRendering.isSelected()));

        renderingMenu.add(gameRendering);
        renderingMenu.add(worldRendering);
        renderingMenu.add(weatherRendering);
        renderingMenu.add(entityRendering);
        renderingMenu.add(tileEntityRendering);

        //==-- NETWORK --==//
        entitySpawning.addActionListener((exd) -> Supervisor.SupervisorModule.CONFIG.network().setIgnoreEntitySpawns(entitySpawning.isSelected()));
        explosionSpawning.addActionListener((exd) -> Supervisor.SupervisorModule.CONFIG.network().setIgnoreExplosionSpawns(explosionSpawning.isSelected()));

        networkMenu.add(entitySpawning);
        networkMenu.add(explosionSpawning);

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
