package me.videogamesm12.wnt.blackbox.window.tab;

import me.videogamesm12.wnt.supervisor.Supervisor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class MainTab extends ScrollableTab
{
    private final JTextArea textArea = new JTextArea();

    public MainTab()
    {
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setLineWrap(true);

        setup();
    }

    @Override
    public JComponent getContentComponent()
    {
        return textArea;
    }

    @Override
    public void update()
    {
        textArea.setText(StringUtils.join(Supervisor.getInstance().getF3Info(), "\n"));
    }
}
