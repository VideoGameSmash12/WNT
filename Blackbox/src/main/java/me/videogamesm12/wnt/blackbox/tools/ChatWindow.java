/*
 * Copyright (c) 2023 Video
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

package me.videogamesm12.wnt.blackbox.tools;

import me.videogamesm12.wnt.supervisor.event.HUDMessageAdded;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatWindow extends JFrame implements HUDMessageAdded
{
    public static ChatWindow INSTANCE = null;
    //--
    private JLabel label = new JLabel("Message:");
    //--
    private JScrollPane scrollPane = new JScrollPane();
    private JTextArea chatArea = new JTextArea();
    //--
    private JTextField messageField = new JTextField();
    private JButton sendButton = new JButton("Send");

    public ChatWindow()
    {
        setTitle("Chat Window");
        setMinimumSize(new Dimension(640, 360));
        setPreferredSize(new Dimension(640, 360));
        //--
        chatArea.setEditable(false);
        chatArea.setColumns(20);
        chatArea.setRows(5);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        //--
        scrollPane.setViewportView(chatArea);
        //--
        messageField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.VK_ENTER)
                    sendMessage(messageField.getText());
            }
        });
        sendButton.addActionListener((event) -> {
            sendMessage(messageField.getText());
        });
        //--
        GroupLayout pLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(pLayout);
        //--
        pLayout.setHorizontalGroup(
            pLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(pLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane)
                        .addGroup(pLayout.createSequentialGroup()
                            .addComponent(label)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(messageField, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sendButton)))
                    .addContainerGap()));
        pLayout.setVerticalGroup(
            pLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, pLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                    .addContainerGap()
                    .addGroup(pLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(messageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(sendButton))
                    .addContainerGap())
        );

        pack();

        HUDMessageAdded.EVENT.register(this);

        INSTANCE = this;
    }

    public static void sendMessage(String text)
    {
        if (MinecraftClient.getInstance().getNetworkHandler() == null)
        {
            return;
        }

        if (text.startsWith("/"))
        {
            MinecraftClient.getInstance().getNetworkHandler().sendChatCommand(text.substring(1));
        }
        else
        {
            MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(text);
        }
    }

    @Override
    public ActionResult onMessageAdded(Text message)
    {
        chatArea.append(message.getString() + "\n");
        if (chatArea.getSelectedText() == null)
        {
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }

        return ActionResult.PASS;
    }
}
