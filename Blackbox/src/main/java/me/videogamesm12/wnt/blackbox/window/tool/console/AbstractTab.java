package me.videogamesm12.wnt.blackbox.window.tool.console;

import me.videogamesm12.wnt.supervisor.Supervisor;
import net.minecraft.text.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class AbstractTab extends JPanel
{
    private final JTextArea textArea = new JTextArea();

    public AbstractTab()
    {
        final JScrollPane scroll = new JScrollPane();

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        scroll.setViewportView(textArea);

        final JLabel besidesField = new JLabel("Input:");
        final JTextField field = new JTextField();
        final JButton sendButton = new JButton("Send");

        field.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    try
                    {
                        send(field.getText());
                    }
                    catch (Throwable ex)
                    {
                        showMessage(ex.getMessage());
                    }
                }
            }
        });
        sendButton.addActionListener((e) ->
        {
            try
            {
                send(field.getText());
            }
            catch (Throwable ex)
            {
                showMessage(ex.getMessage());
            }
        });

        GroupLayout pLayout = new GroupLayout(this);
        setLayout(pLayout);
        //--
        pLayout.setHorizontalGroup(
                pLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(scroll)
                                        .addGroup(pLayout.createSequentialGroup()
                                                .addComponent(besidesField)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(field, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(sendButton)))
                                .addContainerGap()));
        pLayout.setVerticalGroup(
                pLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, pLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                                .addContainerGap()
                                .addGroup(pLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(besidesField)
                                        .addComponent(field, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sendButton))
                                .addContainerGap())
        );
    }

    public void showMessage(Text text)
    {
        if (shouldDisplay(text))
        {
            showMessage(text.getString());
        }
    }

    public void showMessage(String text)
    {
        textArea.append(text + "\n");

        if (textArea.getSelectedText() == null)
        {
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    public void clear()
    {
        textArea.setText(null);
    }

    protected void send(String messageOrCommand)
    {
        if (messageOrCommand.startsWith("/"))
        {
            Supervisor.getInstance().runCommand(messageOrCommand.substring(1));
        }
        else
        {
            Supervisor.getInstance().chatMessage(messageOrCommand);
        }
    }

    /**
     * Filters the message to display.
     * @param message   Text
     * @return          True if the message should go through.
     */
    public abstract boolean shouldDisplay(Text message);

    /**
     * Returns the intended tab name.
     * @return String
     */
    public abstract String name();
}
