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

import me.videogamesm12.wnt.WNT;
import me.videogamesm12.wnt.blackbox.tools.ChatWindow;

import javax.swing.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.Objects;

public class ToolsMenu extends JMenu
{
    public ToolsMenu()
    {
        super("Tools");

        JMenuItem chatWindow = new JMenuItem("Chat");
        chatWindow.addActionListener((event) -> Objects.requireNonNullElseGet(ChatWindow.INSTANCE, ChatWindow::new).setVisible(true));
        JMenuItem dumpThreads = new JMenuItem("Dump thread information");
        dumpThreads.addActionListener((event) -> Arrays.stream(ManagementFactory.getThreadMXBean().dumpAllThreads(true, true)).forEach(this::dumpThread));

        add(chatWindow);
        addSeparator();
        add(dumpThreads);
    }

    /**
     * Dumps thread information to the logs.
     * @param thread    ThreadInfo
     */
    private void dumpThread(ThreadInfo thread)
    {
        WNT.getLogger().info("--== THREAD DUMP - " + thread.getThreadName() + " (" + thread.getThreadState() + ") ==--");
        WNT.getLogger().info("DETAILS: " + threadToFormat(thread));
        WNT.getLogger().info("STACKTRACE: ");
        Arrays.stream(thread.getStackTrace()).forEach(stack -> WNT.getLogger().info(stack));
        WNT.getLogger().info("--== END DUMP ==--");
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
