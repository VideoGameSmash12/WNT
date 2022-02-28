package me.videogamesm12.w95.supervisorgui.tools;

import javax.swing.*;
import java.awt.*;

public class ChatWindow extends JFrame
{
    public static ChatWindow INSTANCE = null;
    //--
    private JPanel panel = new JPanel();
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
        chatArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        //--
        scrollPane.setViewportView(chatArea);
        //--
        GroupLayout layout = new GroupLayout(panel);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(label)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(messageField)
                            /*.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)*/))
                    .addContainerGap()));
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label)
                        .addComponent(messageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        /*.addComponent(sendButton)*/)
                    .addContainerGap())
        );

        add(panel);

        INSTANCE = this;
    }
}
