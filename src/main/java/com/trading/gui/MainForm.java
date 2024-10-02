package com.trading.gui;

import com.trading.EWrapperImpl;
import com.trading.scan.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainForm {
    private final EWrapperImpl wrapper;
    private  List <String> tickers;
    public MainForm(EWrapperImpl wrapper, List<String> tickers){
        this.tickers = tickers;
        this.wrapper = wrapper;
    }

    private JPanel textFiledWithLabel(String labelText, JTextField textField){
        JPanel panel = new JPanel(new FlowLayout());
        JLabel jLabel = new JLabel(labelText);
        panel.add(jLabel);
        //JTextField textField = new JTextField(10);
        //textField.setText(text);
        panel.add(textField);
        return panel;
    }
    public void display() {
        JFrame frame = new JFrame("Trading scanner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(new BorderLayout());

        // Create a button
        JButton showListButton = new JButton("Run Volume scanner");
        JButton searchCross = new JButton("CrossEnterPoint search execute trade");


        // Create a panel to add the button
        JPanel panel = new JPanel();
        JPanel crossPanel = new JPanel();

        JButton stockAboveSmaSearchButton = new JButton("Stock above 200 sma");
       // frame.add(stockAboveSmaSearchButton, BorderLayout.SOUTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(showListButton);
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
        shortEma.setText("12");
        JTextField longEma = new JTextField();
        longEma.setText("26");
        JTextField capitalField = new JTextField();
        capitalField.setText("10000");
        JTextField riskPercentField = new JTextField();
        riskPercentField.setText("1");
        crossPanel.add(textFiledWithLabel("totalAmount", capitalField));
        crossPanel.add(textFiledWithLabel("risk percent", riskPercentField));
        crossPanel.add(textFiledWithLabel("Short Ema", shortEma));
        crossPanel.add(textFiledWithLabel("Long Ema", longEma));

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
                double capital = Double.parseDouble(capitalField.getText());
                int shortEmaValue= Integer.parseInt(shortEma.getText());
                int longEmaValue= Integer.parseInt(longEma.getText());
                Scan scan = new CrossScan(shortEmaValue,longEmaValue);
                List<String> list = scan.scan(wrapper, new PlaceOrderAction(wrapper,capital,riskPercent),tickers);
                List<String>  messageList = new ArrayList<>();
                messageList.add("Ticker is placed");
                messageList.add(list.get(0));
                updateTextArea(textArea,messageList);
            }
        });

        stockAboveSmaSearchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Scan scanner = new SmaScan();
                updateTextArea(textArea, scanner.scan(wrapper, new SaveTickerAction(), tickers));
            }
        });
        // Add an ActionListener to the button
        showListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scan scanner = new VolumeScan();
                List <String> tickerList= scanner.scan(wrapper, new SaveTickerAction(), tickers);
                updateTextArea(textArea, tickerList);

            }
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
