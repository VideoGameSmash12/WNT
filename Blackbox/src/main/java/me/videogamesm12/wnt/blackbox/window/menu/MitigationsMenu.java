package me.videogamesm12.wnt.blackbox.window.menu;

import me.videogamesm12.wnt.supervisor.Configuration;
import me.videogamesm12.wnt.supervisor.Supervisor;

import javax.swing.*;

public class MitigationsMenu extends JMenu
{
    public MitigationsMenu()
    {
        super("Mitigations");

        // GLOBAL
        final Configuration config = Supervisor.getConfig();

        // RENDERING
        add(setupRenderingMenu(config));    // RENDER
        add(setupNetworkingMenu(config));   // NETWORK
        addSeparator();                     //--
        add(setupDrasticMenu());            // DRASTIC
    }

    private JMenu setupDrasticMenu()
    {
        final JMenu menu = new JMenu("Drastic");
        menu.setToolTipText("Drastic actions you can take if necessary.");

        final JMenuItem crash = new JMenuItem("Crash");
        crash.setToolTipText("Forces the client to crash the next time it tries to render something.");
        crash.addActionListener((e) -> Supervisor.getInstance().getFlags().setSupposedToCrash(true));
        menu.add(crash);
        //--
        final JMenuItem disconnect = new JMenuItem("Disconnect");
        disconnect.setToolTipText("Disconnects you from the server you are currently connected to.");
        disconnect.addActionListener((e) -> Supervisor.getInstance().disconnect());
        menu.add(disconnect);
        //--
        final JMenuItem shutdownSafe = new JMenuItem("Shutdown");
        shutdownSafe.setToolTipText("Safely closes the game.");
        shutdownSafe.addActionListener((e) -> Supervisor.getInstance().shutdownSafely());
        menu.add(shutdownSafe);
        //--
        final JMenuItem shutdownForce = new JMenuItem("Shutdown (Forcefully)");
        shutdownForce.setToolTipText("Forcefully closes the game whilst still running necessary shutdown hooks.");
        shutdownForce.addActionListener((e) -> Supervisor.getInstance().shutdownForcefully());
        menu.add(shutdownForce);
        //--
        final JMenuItem shutdownNuclear = new JMenuItem("Shutdown (Nuclear)");
        shutdownNuclear.setToolTipText("Forcefully closes the game without running the necessary shutdown hooks.\nYou should never do this unless all else has failed. This will cause problems.");
        shutdownNuclear.addActionListener((e) -> Supervisor.getInstance().shutdownNuclear());
        menu.add(shutdownNuclear);

        return menu;
    }

    private JMenu setupNetworkingMenu(Configuration config)
    {
        final JMenu menu = new JMenu("Network");
        menu.setToolTipText("Network-related mitigations");
        final Configuration.Network network = config.getNetworkSettings();

        final JCheckBoxMenuItem entitySpawning = new JCheckBoxMenuItem("Ignore entity spawning", network.isIgnoringEntitySpawns());
        entitySpawning.setToolTipText("Ignores packets for entity spawning.");
        entitySpawning.addActionListener((e) -> network.setIgnoringEntitySpawns(entitySpawning.isSelected()));
        menu.add(entitySpawning);
        //--
        final JCheckBoxMenuItem explosionSpawning = new JCheckBoxMenuItem("Ignore explosions", network.isIgnoringExplosions());
        explosionSpawning.setToolTipText("Ignores packets for spawning explosions. Block updates do still occur.");
        explosionSpawning.addActionListener((e) -> network.setIgnoringExplosions(explosionSpawning.isSelected()));
        menu.add(explosionSpawning);
        //--
        final JCheckBoxMenuItem lightUpdates = new JCheckBoxMenuItem("Ignore light updates", network.isIgnoringLightUpdates());
        lightUpdates.setToolTipText("Ignores packets for light updates. Combats a known client lag exploit related to this.");
        lightUpdates.addActionListener((e) -> network.setIgnoringLightUpdates(lightUpdates.isSelected()));
        menu.add(lightUpdates);
        //--
        final JCheckBoxMenuItem particleSpawning = new JCheckBoxMenuItem("Ignore particle spawning", network.isIgnoringParticleSpawns());
        particleSpawning.setToolTipText("Ignores packets for particle spawning.");
        particleSpawning.addActionListener((e) -> network.setIgnoringParticleSpawns(particleSpawning.isSelected()));
        menu.add(particleSpawning);

        return menu;
    }

    private JMenu setupRenderingMenu(Configuration config)
    {
        final JMenu menu = new JMenu("Render");
        menu.setToolTipText("Rendering-related mitigations");
        final Configuration.Rendering rendering = config.getRenderingSettings();

        final JCheckBoxMenuItem gameRendering = new JCheckBoxMenuItem("Disable rendering completely", rendering.isGameRenderingDisabled());
        gameRendering.setToolTipText("Entirely disables any and all rendering for the game.");
        gameRendering.addActionListener((e) -> rendering.setGameRenderingDisabled(gameRendering.isSelected()));
        menu.add(gameRendering);
        //--
        final JCheckBoxMenuItem worldRendering = new JCheckBoxMenuItem("Disable world rendering", rendering.isWorldRenderingDisabled());
        worldRendering.setToolTipText("Prevents the game from rendering the world itself.");
        worldRendering.addActionListener((e) -> rendering.setWorldRenderingDisabled(worldRendering.isSelected()));
        menu.add(worldRendering);
        //--
        final JCheckBoxMenuItem weatherRendering = new JCheckBoxMenuItem("Disable weather rendering", rendering.isWeatherRenderingDisabled());
        weatherRendering.setToolTipText("Prevents the game from rendering the weather. Helpful in case the particles cause FPS drops.");
        weatherRendering.addActionListener((e) -> rendering.setWeatherRenderingDisabled(weatherRendering.isSelected()));
        menu.add(weatherRendering);
        //--
        final JCheckBoxMenuItem entityRendering = new JCheckBoxMenuItem("Disable entity rendering", rendering.isEntityRenderingDisabled());
        entityRendering.setToolTipText("Prevents the game from rendering entities.");
        entityRendering.addActionListener((e) -> rendering.setEntityRenderingDisabled(entityRendering.isSelected()));
        menu.add(entityRendering);
        //--
        final JCheckBoxMenuItem tileEntityRendering = new JCheckBoxMenuItem("Disable tile entity rendering", rendering.isTileEntityRenderingDisabled());
        tileEntityRendering.setToolTipText("Prevents the game from rendering tile entities (e.g. chests, shulker boxes, skulls, etc).");
        tileEntityRendering.addActionListener((e) -> rendering.setTileEntityRenderingDisabled(tileEntityRendering.isSelected()));
        menu.add(tileEntityRendering);

        return menu;
    }
}
