package me.videogamesm12.wnt.blackbox.menus;

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.tools.ChatWindow;

import javax.swing.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Objects;

public class ToolsMenu extends JMenu
{
    private final JMenuItem chatWindow = new JMenuItem("Chat");
    //--
    private final JMenuItem dumpThreads = new JMenuItem("Dump thread information");

    public ToolsMenu()
    {
        super("Tools");

        chatWindow.addActionListener((event) -> {
            Objects.requireNonNullElseGet(ChatWindow.INSTANCE, ChatWindow::new).setVisible(true);
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
        WNT.LOGGER.info("--== THREAD DUMP - " + thread.getThreadName() + " (" + thread.getThreadState() + ") ==--");
        WNT.LOGGER.info("DETAILS: " + threadToFormat(thread));
        WNT.LOGGER.info("STACK: ");

        for (StackTraceElement stack : thread.getStackTrace())
            WNT.LOGGER.info(stack);

        WNT.LOGGER.info("--== END DUMP ==--");
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
