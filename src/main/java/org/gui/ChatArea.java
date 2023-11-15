package org.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;

public class ChatArea extends JPanel {
    private static ChatArea instance;
    private int lastMessagePosition = 0;

    public static ChatArea getInstance(JFrame parent){
        if (instance == null) {
            instance = new ChatArea(parent);
        }
        return instance;
    }

    private ChatArea(JFrame parent){
        setLayout(null);
    }

    protected void addMessage(String content, String sender){

        Message message = new Message(content, sender);
        message.setBounds(message.x,lastMessagePosition + 20, message.width, message.getHeight());
        lastMessagePosition = lastMessagePosition + 20 + message.getHeight();
        setPreferredSize(new Dimension(0,lastMessagePosition));
        super.add(message);
        revalidate();
        repaint();
    }


    static class Message extends JPanel{
        private final String content;
        private final String sender;
        private final boolean isMyMessage;
        private final Color messageColor;
        private final Color textColor;

        int x;
        int width;
        Date timestamp;

        /**
         * Message constructor.
         * @param content -- message content
         * @param sender -- Sender of the message. If the message is sent by this user, sender should be "Me"
         */
        public Message(String content, String sender){
            this.content = content;
            this.sender = sender;
            this.isMyMessage = "Me".equals(sender);
            this.messageColor = this.isMyMessage? Color.blue : Color.LIGHT_GRAY;
            this.textColor = this.isMyMessage? Color.white : Color.black;
            this.x = isMyMessage ? 30 : 5;
            this.width = 250;
            this.timestamp = new Date();

            setLayout(new BorderLayout());

            JPanel panel = new JPanel(new BorderLayout());

            JTextField header = new JTextField(this.sender);
            header.setBackground(messageColor);
            header.setForeground(textColor);
            header.setFont(header.getFont().deriveFont(Font.BOLD,15.0F));
            header.setEditable(false);
            header.setBorder(new EmptyBorder(0, 0, 0, 0));

            JTextArea inner = new JTextArea(this.content, 0,10);
            inner.setSize(new Dimension(200,inner.getPreferredSize().height));
            inner.setEditable(false);
            inner.setLineWrap(true);
            inner.setWrapStyleWord(true);
            inner.setBackground(messageColor);
            inner.setForeground(textColor);
            inner.setFont(header.getFont().deriveFont(Font.PLAIN,12.0F));
            inner.setBorder(new EmptyBorder(0, 0, 8, 0));

            JTextField footer = new JTextField(this.timestamp.toString());
            footer.setBackground(messageColor);
            footer.setForeground(textColor);
            footer.setHorizontalAlignment(SwingConstants.RIGHT);
            footer.setFont(header.getFont().deriveFont(Font.ITALIC,9.0F));
            footer.setEditable(false);
            footer.setBorder(new EmptyBorder(0, 0, 0, 0));


            panel.add(header, BorderLayout.NORTH);
            panel.add(inner, BorderLayout.CENTER);
            panel.add(footer, BorderLayout.SOUTH);
            panel.setBorder(new EmptyBorder(10, 5, 2, 5));
            panel.setBackground(messageColor);



            if (isMyMessage)
                add(panel,BorderLayout.EAST);
            else
                add(panel,BorderLayout.WEST);
            add(new JPanel(), BorderLayout.CENTER);

            setSize(new Dimension(200, panel.getPreferredSize().height ));
        }



    }
}



