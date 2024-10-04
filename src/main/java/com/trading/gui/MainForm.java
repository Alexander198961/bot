package com.trading.gui;

import com.trading.EWrapperImpl;
import com.trading.scan.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class MainForm {
    private final EWrapperImpl wrapper;
    private final Map <String,List<String>> storageIndexTicker;
    public MainForm(EWrapperImpl wrapper, Map<String,List<String>> tickers){
        this.storageIndexTicker = tickers;
        this.wrapper = wrapper;
    }

    private JPanel componentWithLabel(String labelText, JComponent textField){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel jLabel = new JLabel(labelText);
        panel.add(jLabel);
        panel.add(textField);
        return panel;
    }
    public void display() {
        JFrame frame = new JFrame("Trading scanner");
        JList stockIndexesListUI = new JList(storageIndexTicker.keySet().toArray());
        stockIndexesListUI.setSelectedIndex(1);
        stockIndexesListUI.setVisibleRowCount(1);
        JPanel panelIndexChooser = componentWithLabel("Select index",stockIndexesListUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(new BorderLayout());
        frame.add(panelIndexChooser);
      //  frame.add(stockIndexesListUI);

        // Create a button
        JButton showVolumeButton = new JButton("Run Volume scanner");
        JPanel volumeConfigurationPanel = new JPanel(new BorderLayout());
        JTextField volumeSettings = new JTextField();
        volumeSettings.setText("1.8");
        volumeSettings.setColumns(5);
        volumeConfigurationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        volumeConfigurationPanel.add(volumeSettings);
        volumeConfigurationPanel.add(componentWithLabel("Volume settings",showVolumeButton));
        JButton searchCross = new JButton("CrossEnterPoint search execute trade");


        // Create a panel to add the button
        JPanel panel = new JPanel();
        panel.add(panelIndexChooser);
        JPanel crossPanel = new JPanel();

        JButton stockAboveSmaSearchButton = new JButton("Stock above 200 sma");
       // frame.add(stockAboveSmaSearchButton, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(volumeConfigurationPanel);
        panel.add(volumeSettings);
        //panel.add(showVolumeButton);
        panel.add(stockAboveSmaSearchButton);
        crossPanel.setLayout(new BoxLayout(crossPanel, BoxLayout.Y_AXIS));
        //JTextField shortEma = new JTextField(10);
       // First input field
       // JTextField  longEma= new JTextField(10);
       // JLabel labelShortEma = new JLabel("Short Ema");
       // JLabel labelLongEma = new JLabel("Long Ema");
       // shortEma.setText("7");
       // longEma.setText("21");
        crossPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        crossPanel.add(searchCross);
        JTextField shortEma = new JTextField();
        shortEma.setColumns(4);
        shortEma.setText("12");
        JTextField longEma = new JTextField();
        longEma.setText("26");
        longEma.setColumns(4);
        JTextField capitalField = new JTextField();
        capitalField.setText("10000");
        capitalField.setColumns(8);
        JTextField riskPercentField = new JTextField();
        riskPercentField.setText("1");
        riskPercentField.setColumns(4);
        JTextField stopPercentField = new JTextField();
        stopPercentField.setText("4");
        stopPercentField.setColumns(4);
        JTextField bellowLargeEma = new JTextField();
        bellowLargeEma.setText("5");
        bellowLargeEma.setColumns(4);
        crossPanel.add(componentWithLabel("totalAmount", capitalField));
        crossPanel.add(componentWithLabel("risk %", riskPercentField));
        crossPanel.add(componentWithLabel("Short Ema", shortEma));
        crossPanel.add(componentWithLabel("Long Ema", longEma));
        crossPanel.add(componentWithLabel("stop %", stopPercentField));
        crossPanel.add(componentWithLabel("bellow Large Ema %", bellowLargeEma));

       // crossPanel.add(shortEma);
        //crossPanel.add(longEma);
        Border border = new LineBorder(Color.RED, 2);
        crossPanel.setBorder(border);
        panel.add(crossPanel);

        //panel.add(searchCross);
        frame.add(panel, BorderLayout.NORTH);
        //frame.add(searchCross, BorderLayout.EAST);
        // Create a text area to display the list
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        searchCross.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double riskPercent = Double.parseDouble(riskPercentField.getText());
                double bellowEmaPercent = Double.parseDouble(bellowLargeEma.getText());
                double stopPercent = Double.parseDouble(stopPercentField.getText());
                double capital = Double.parseDouble(capitalField.getText());
                int shortEmaValue= Integer.parseInt(shortEma.getText());
                int longEmaValue= Integer.parseInt(longEma.getText());
                Scan scan = new CrossScan(shortEmaValue,longEmaValue, bellowEmaPercent);
                List<String> list = scan.scan(wrapper, new PlaceOrderAction(wrapper, capital,riskPercent,  stopPercent),storageIndexTicker.get(stockIndexesListUI.getSelectedValue()));
                List<String>  messageList = new ArrayList<>();
                messageList.add(list.get(0));
                updateTextArea(textArea,messageList);
            }
        });

        stockAboveSmaSearchButton.addActionListener(actionEvent -> {
            Scan scanner = new SmaScan();
            updateTextArea(textArea, scanner.scan(wrapper, new SaveTickerAction(), storageIndexTicker.get(stockIndexesListUI.getSelectedValue())));
        });
        // Add an ActionListener to the button
        showVolumeButton.addActionListener(e -> {
            double volumePercent = Double.parseDouble(volumeSettings.getText());
            Scan scanner = new VolumeScan(volumePercent);
            List <String> tickerList= scanner.scan(wrapper, new SaveTickerAction(), storageIndexTicker.get(stockIndexesListUI.getSelectedValue()));
            updateTextArea(textArea, tickerList);

        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private  void updateTextArea(JTextArea textArea,List <String> tickerList){
        textArea.selectAll();
        textArea.replaceSelection("");
        for (String item : tickerList) {
            textArea.append(item + "\n"); // append each item to the text area
        }
    }
}
