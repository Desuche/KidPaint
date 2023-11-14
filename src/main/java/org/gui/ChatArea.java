package org.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChatArea extends JPanel {
    private static ChatArea instance;

    public static ChatArea getInstance(JFrame parent){
        if (instance == null)
            instance = new ChatArea(parent);
        return instance;
    }

    private ChatArea(JFrame parent){
        setLayout(new GridLayout(0, 1, 10, 10));
    }

    protected void add(Message message){
        super.add(message);
        System.out.println("adding message");
    }


    static class Message extends JPanel{
        private final String content;
        private final String sender;
        private final boolean isMyMessage;
        Color messageColor;

        /**
         * Message constructor.
         * @param content -- message content
         * @param sender -- Sender of the message. If the message is sent by this user, sender should be "Me"
         */
        public Message(String content, String sender){
            this.content = content;
            this.sender = sender;
            this.isMyMessage = "Me".equals(sender);
            messageColor = this.isMyMessage? Color.green : Color.blue;
            setSize(new Dimension(200, 100));
            setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JTextField header = new JTextField(sender);
            header.setBackground(messageColor);
            header.setEditable(false);
            header.setBorder(new EmptyBorder(0, 0, 0, 0));
            panel.add(header, BorderLayout.NORTH);
            JTextArea inner = new JTextArea(content);
            inner.setPreferredSize(new Dimension(200, 100));
            inner.setEditable(false);
            inner.setLineWrap(true);
            inner.setBackground(messageColor);
            inner.setBorder(new EmptyBorder(0, 0, 0, 0));
            panel.add(inner, BorderLayout.CENTER);
            panel.setBackground(messageColor);
            panel.setPreferredSize(new Dimension(200, 100));

            if (isMyMessage)
                add(panel,BorderLayout.WEST);
            else
                add(panel,BorderLayout.EAST);
            add(new JPanel(), BorderLayout.CENTER);
        }



    }
}



