package com.trading.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HostPortInputDialog extends JDialog {
    private JTextField hostField;
    private JTextField portField;
    private boolean submitted = false;

    public HostPortInputDialog(Frame parent) {
        super(parent, "Enter Host and Port", true);
        setupForm(parent);
    }

    private void setupForm(Frame parent) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel hostLabel = new JLabel("Host:");
        hostField = new JTextField(20);
        hostField.setText("localhost");

        JLabel portLabel = new JLabel("Port:");
        portField = new JTextField(20);
        portField.setText("7496");

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitted = true;
                dispose(); // close the dialog
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitted = false;
                dispose();
                System.exit(10);
                // close the dialog
            }
        });

        panel.add(hostLabel);
        panel.add(hostField);
        panel.add(portLabel);
        panel.add(portField);
        panel.add(submitButton);
        panel.add(cancelButton);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public String getHost() {
        return hostField.getText();
    }

    public int getPort() {
        try {
            return Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            return -1; // Invalid port
        }
    }

    public boolean isSubmitted() {
        return submitted;
    }
}