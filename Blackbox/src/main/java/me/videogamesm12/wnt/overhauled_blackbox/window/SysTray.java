package me.videogamesm12.wnt.overhauled_blackbox.window;

import lombok.Getter;
import me.videogamesm12.wnt.overhauled_blackbox.Blackbox;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SysTray
{
    @Getter
    private TrayIcon icon;

    public SysTray(Blackbox blackbox)
    {
        if (SystemTray.isSupported())
        {
            icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(
                    Blackbox.class.getClassLoader().getResource("assets/wnt-blackbox/supervisor_icon.png")),
                    "Blackbox - Click to Open");
            icon.setImageAutoSize(true);
            icon.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    blackbox.openWindow();
                }
            });
        }
    }

    public void addIcon() throws AWTException
    {
        SystemTray.getSystemTray().add(icon);
    }
}
