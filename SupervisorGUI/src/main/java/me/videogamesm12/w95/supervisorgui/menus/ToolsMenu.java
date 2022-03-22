package me.videogamesm12.w95.supervisorgui.menus;

import me.videogamesm12.w95.W95;
import me.videogamesm12.w95.supervisorgui.tools.ChatWindow;

import javax.swing.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

public class ToolsMenu extends JMenu
{
    private final JMenuItem chatWindow = new JMenuItem("Chat");
    //--
    private final JMenuItem dumpThreads = new JMenuItem("Dump thread information");

    public ToolsMenu()
    {
        super("Tools");

        chatWindow.addActionListener((event) -> {
            if (ChatWindow.INSTANCE != null)
            {
                ChatWindow.INSTANCE.setVisible(true);
            }
            else
            {
                new ChatWindow().setVisible(true);
            }
        });

        dumpThreads.addActionListener((event) -> {
            for (ThreadInfo info : ManagementFactory.getThreadMXBean().dumpAllThreads(true, true))
            {
                dumpThread(info);
            }
        });

        add(chatWindow);
        add(new JPopupMenu.Separator());
        add(dumpThreads);
    }

    /**
     * Dumps thread information to the logs.
     * @param thread    ThreadInfo
     */
    private void dumpThread(ThreadInfo thread)
    {
        W95.LOGGER.info("--== THREAD DUMP - " + thread.getThreadName() + " (" + thread.getThreadState() + ") ==--");
        W95.LOGGER.info("DETAILS: " + threadToFormat(thread));
        W95.LOGGER.info("STACK: ");

        for (StackTraceElement stack : thread.getStackTrace())
            W95.LOGGER.info(stack);

        W95.LOGGER.info("--== END DUMP ==--");
    }

    /**
     * Takes certain details from threads and sticks them into a single String.
     * @param info  ThreadInfo
     * @return      String
     */
    private String threadToFormat(ThreadInfo info)
    {
        return String.format("Suspended: %s, Native: %s", info.isSuspended() ? "Yes" : "No", info.isInNative() ? "Yes" : "No");
    }
}
