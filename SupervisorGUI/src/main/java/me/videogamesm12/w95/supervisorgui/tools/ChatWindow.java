package me.videogamesm12.w95.supervisorgui.tools;

import me.videogamesm12.w95.supervisor.event.HUDMessageAdded;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

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
        chatArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        //--
        scrollPane.setViewportView(chatArea);
        //--
        sendButton.addActionListener((event) -> {
            if (MinecraftClient.getInstance().getNetworkHandler() != null && MinecraftClient.getInstance().player != null)
                MinecraftClient.getInstance().player.sendChatMessage(messageField.getText());
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

    @Override
    public ActionResult onMessageAdded(MessageType type, Text message, UUID sender)
    {
        chatArea.append(message.getString() + "\n");
        if (chatArea.getSelectedText() == null)
        {
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }

        return ActionResult.PASS;
    }
}
