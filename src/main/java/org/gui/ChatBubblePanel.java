package org.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatBubblePanel extends JPanel {
    private String message;
    private static ChatArea chatArea;
    private int bubbleWidth = 100; // Fixed width of the bubble

    public ChatBubblePanel(String message) {
        this.message = message;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(bubbleWidth, 0)); // Set initial height to 0
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int bubbleHeight = getHeight() - 20; // Adjust padding
        int radius = 10;
        int x = 10; // Adjust padding
        int y = 10; // Adjust padding

//        g2d.setColor(Color.lightGray); // Set bubble color
//        g2d.fillRoundRect(x, y, bubbleWidth, bubbleHeight, radius, radius);

        g2d.setColor(Color.black); // Set text color
        g2d.drawString(message, x + 10, y + 20); // Adjust text position

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int textWidth = fontMetrics.stringWidth(message) + 20; // Adjust padding
        int textHeight = fontMetrics.getHeight() + 20; // Adjust padding
        int preferredHeight = Math.max(textHeight, 30); // Minimum height of 30
        return new Dimension(bubbleWidth, preferredHeight);
    }

    private static void onTextInputted(String text) {
        SwingUtilities.invokeLater(() -> {
//		chatArea.setText(chatArea.getText() + text + "\n");
            System.out.println("Text inputted");
            chatArea.add(new ChatArea.Message(text, "Me"));
            chatArea.add(new ChatArea.Message("I have responded", "Person X"));
            chatArea.revalidate();
            chatArea.repaint();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chat Bubble Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel home = new JPanel();
            home.setLayout(new BorderLayout());

            JTextField msgField = new JTextField();	// text field for inputting message
            home.add(msgField, BorderLayout.SOUTH);



            // handle key-input event of the message field
            msgField.addKeyListener(new KeyListener() {
                @Override public void keyTyped(KeyEvent e) {}
                @Override public void keyPressed(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == 10) {		// if the user press ENTER
                        onTextInputted(msgField.getText());
                        msgField.setText("");
                    }
                }

            });

//            JPanel panel = new JPanel();
//            panel.setLayout(new GridLayout(0, 1, 10, 10));
////            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
////            ChatBubblePanel bubble1 = new ChatBubblePanel("Hello!");
////            panel.add(bubble1);
////
////            ChatBubblePanel bubble2 = new ChatBubblePanel("Hi there! This is a longer message.Hi there! This is a longer message.Hi there! This is a longer message.Hi there! This is a longer message.");
////            bubble2.setBackground(Color.cyan); // Customize bubble color
////            bubble2.setForeground(Color.white); // Customize text color
////            panel.add(bubble2);
//
//            panel.add(new ChatArea.Message("Hi there", "Me"));
//            panel.add(new ChatArea.Message("Hello", "John"));

            chatArea = ChatArea.getInstance(frame);
            chatArea.add(new ChatArea.Message("Hi there", "Me"));
            chatArea.add(new ChatArea.Message("Hello", "John"));
            chatArea.add(new ChatArea.Message("Hello", "John"));
            chatArea.add(new ChatArea.Message("Hi there JPanel panel = new JPanel();\n" +
                    "//            panel.setLayout(new GridLayout(0, 1, 10, 10));\n" +
                    "////            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));\n" +
                    "//\n" +
                    "////            ChatBubblePanel bubble1 = new ChatBubblePanel(\"Hello!\");\n" +
                    "////            panel.add(bubble1);\n" +
                    "////\n" +
                    "////            ChatBubblePanel bubble2 = new ChatBubblePanel(\"Hi there! This is a longer message.Hi there! This is a longer message.Hi there! This is a longer message.Hi there! This is a longer message.\");\n" +
                    "////            bubble2.setBackground(Color.cyan); // Customize bubble color\n" +
                    "////            bubble2.setForeground(Color.white); // Customize text color\n" +
                    "////            panel.add(bubble2);\n" +
                    "//\n" +
                    "//            panel.add(new ChatArea.Message(\"Hi there\", \"Me\"));\n" +
                    "//            panel.add(new ChatArea.Message(\"Hello\", \"John\"));", "Me"));
            chatArea.add(new ChatArea.Message("Hello", "John"));
            chatArea.add(new ChatArea.Message("Hello", "John"));
            chatArea.add(new ChatArea.Message("Hi there", "Me"));
            chatArea.add(new ChatArea.Message("Hello", "John"));
            chatArea.add(new ChatArea.Message("Hello", "John"));
            chatArea.add(new ChatArea.Message("Hi there", "Me"));
            chatArea.add(new ChatArea.Message("Hello", "John"));
            chatArea.add(new ChatArea.Message("Hello", "John"));


            JScrollPane scrollPaneRight = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPaneRight.setPreferredSize(new Dimension(300, 700));

            home.add(scrollPaneRight);
            frame.getContentPane().add(home);
            frame.pack();
            frame.setVisible(true);
            System.out.println(chatArea.getHeight());
        });
    }
}