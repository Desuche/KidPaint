package org.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudioSelectionPopup extends JDialog {
    private static StudioSelectionPopup instance = null;
    private final JFrame parent;


    public static StudioSelectionPopup getInstance(JFrame parent) {
        if (instance == null)
            instance = new StudioSelectionPopup(parent);
        return instance;
    }


    private StudioSelectionPopup(JFrame parent){
        this.parent = parent;
        List<String[]> servers = findServers();

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15,15,5,15));

        for (String[] server : servers) {
            String studioName = server[0]+ "'s studio";
            JButton button = new JButton(studioName);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    joinServer(server);
                }
            });
            panel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        JButton button = new JButton("Start your own studio");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                startServer();
            }
        });
        button.setBorder(new EmptyBorder(20,5,20,5));


        JPanel container = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Choose a studio");
        label.setFont(label.getFont().deriveFont(Font.BOLD,15.0F));
        add(label, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(button, BorderLayout.SOUTH);
        container.setPreferredSize(new Dimension(200,300));
        getContentPane().add(container);
        pack();
        setLocationRelativeTo(parent);

    }



    private List<String[]> findServers(){

        //TODO
        //find servers via network (another thread: this will change method signature)

        List<String[]> servers = new ArrayList<>();
        servers.add(new String[]{"Destiny", "128.0.168.95"});
        servers.add(new String[]{"Ritik", "128.0.168.135"});
        servers.add(new String[]{"Samir", "128.0.168.210"});

        return servers;
    }

    private void joinServer(String[] server){
        JOptionPane.showMessageDialog(parent,"Joining "+server[0] + "'s studio ");
        //TODO
    }

    private void startServer(){
        //Server.getInstance().start();
        //TODO
        JOptionPane.showMessageDialog(parent,"Started your own studio");
    }

}