package me.videogamesm12.wnt.overhauled_blackbox.window.tool.console;

import me.videogamesm12.wnt.supervisor.event.HUDMessageAdded;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import javax.swing.*;
import java.awt.*;

public class Console extends JFrame implements HUDMessageAdded
{
    private final JTabbedPane tabs;

    public Console()
    {
        super("Blackbox Console");
        setName("Blackbox Console");
        setMinimumSize(new Dimension(640, 360));
        setPreferredSize(new Dimension(640, 400));

        // Set up the tabs
        this.tabs = new JTabbedPane();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabs,
                GroupLayout.Alignment.TRAILING));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabs,
                GroupLayout.Alignment.TRAILING));
        addTab(new MainChatTab());

        // If the Fantasia tab fails to initialize for whatever reason, it won't cause the Blackbox to fall with it.
        try
        {
            addTab(new FantasiaTab());
        }
        catch (Throwable ignored)
        {
        }

        // Set up the menu bar
        final JMenuBar menuBar = new JMenuBar();

        // OUTPUT
        final JMenu output = new JMenu("Output");
        final JMenuItem clearOutput = new JMenuItem("Clear Output");
        clearOutput.addActionListener((e) -> {
            if (tabs.getSelectedComponent() instanceof AbstractTab tab)
            {
                tab.clear();
            }
        });
        output.add(clearOutput);
        menuBar.add(output);

        setJMenuBar(menuBar);

        // Finally, we do this
        pack();

        // Register event listener
        HUDMessageAdded.EVENT.register(this);
    }

    @Override
    public ActionResult onMessageAdded(Text message)
    {
        // Future-proofing; I might add integration for AdvancedChat in the future so why not lay the groundwork for that?
        for (int i = 0; i < tabs.getTabCount(); i++)
        {
            if (tabs.getComponentAt(i) instanceof AbstractTab tab)
            {
                tab.showMessage(message);
            }
        }

        return ActionResult.PASS;
    }

    public void addTab(AbstractTab tab)
    {
        tabs.addTab(tab.name(), tab);
    }
}
