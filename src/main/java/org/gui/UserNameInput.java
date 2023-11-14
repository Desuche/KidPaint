package org.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserNameInput extends JDialog {
    private static UserNameInput instance = null;
    private final JTextField inputField;
    private String inputText;

    public static UserNameInput getInstance(JFrame parent) {
        if (instance == null)
            instance = new UserNameInput(parent);
        return instance;
    }

    private UserNameInput(JFrame parent) {
        super(parent, "User Name", true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setSize(300, 150);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        add(inputField, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputField.getText().isEmpty()) return;
                inputText = inputField.getText();
                ((UI)parent).loadusername(inputText);
                dispose();

            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    protected String getInputText(){
        return inputText;
    }
}
