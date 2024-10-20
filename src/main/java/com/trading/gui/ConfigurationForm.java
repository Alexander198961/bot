package com.trading.gui;

import com.ib.client.EClientSocket;
import com.trading.EWrapperImpl;
import com.trading.IBSignalHandler;
import com.trading.config.GlobalConfiguration;
import com.trading.scheduler.TaskScheduler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationForm extends CommonForm {
    public ConfigurationForm() {
        this.wrapper = new EWrapperImpl();
    }

    private EWrapperImpl wrapper;

    private JTextField hostField = new JTextField(20);
    private JTextField portField = new JTextField(20);
    private JFrame configurationFrame = new JFrame("Configuration Form");

    public void show() {
        hostField.setText("localhost");
        portField.setText("7496");


        // Create Save button with an ActionListener
        JButton saveButton = new JButton("Save Configuration");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalConfiguration globalConfiguration = new GlobalConfiguration();
                String host = hostField.getText();
                String port = portField.getText();
                globalConfiguration.setHost(host);
                globalConfiguration.setPort(Integer.parseInt(port));
                // Display entered values (or you can save them to a file or use them)
                JOptionPane.showMessageDialog(configurationFrame,
                        "Host: " + host + "\nPort: " + port,
                        "Configuration Saved",
                        JOptionPane.INFORMATION_MESSAGE);

                System.out.println("ib tws port=" + port);
                System.out.println("ib tws host=" + host);
                int portIbkr = globalConfiguration.getPort();
                assert portIbkr > 0;
                assert !host.isEmpty();
                wrapper = new EWrapperImpl();
                IBSignalHandler ibSignalHandler = new IBSignalHandler(wrapper, portIbkr, host);
                ibSignalHandler.run();
                EClientSocket m_client = wrapper.getClient();
                assert m_client.isConnected();

                if (m_client.isConnected()) {
                    System.out.println("Connected to TWS");
                } else {
                    System.out.println("Couldn't connect");
                    System.exit(10);
                }
                /*
                TaskScheduler taskScheduler = new TaskScheduler(wrapper, te);
                taskScheduler.run();
                MainForm mainForm = new MainForm();
                mainForm.display();
                configurationFrame.setVisible(false);

                 */
            }


        });

        // Create panels for host and port
        JPanel hostPanel = componentWithLabel("IBKR Host:", hostField, null);
        JPanel portPanel = componentWithLabel("IBKR Port:", portField, null);

        // Set layout for the frame
        configurationFrame.setLayout(new BorderLayout());

        // Add components to the frame
        JPanel inputPanel = new JPanel(new GridLayout(2, 1)); // Panel to hold input fields
        inputPanel.add(hostPanel);
        inputPanel.add(portPanel);

        configurationFrame.add(inputPanel, BorderLayout.CENTER);
        configurationFrame.add(saveButton, BorderLayout.SOUTH);

        // Configure frame settings
        configurationFrame.setSize(400, 150);
        configurationFrame.setVisible(true);
        configurationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
}
