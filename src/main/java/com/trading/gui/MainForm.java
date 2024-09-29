package com.trading.gui;

import com.ib.client.Bar;
import com.trading.EWrapperImpl;
import com.trading.support.Calculator;
import com.trading.support.VolumeCalculator;
import com.trading.api.USStockContract;
import com.trading.support.reader.TickerReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainForm {
    private final EWrapperImpl wrapper;
    public MainForm(EWrapperImpl wrapper){
        this.wrapper = wrapper;
    }

    public void display() {
        JFrame frame = new JFrame("List Display Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(new BorderLayout());

        // Create a button
        JButton showListButton = new JButton("Show List");

        // Create a panel to add the button
        JPanel panel = new JPanel();
        panel.add(showListButton);
        frame.add(panel, BorderLayout.NORTH);
        JButton stockAboveSmaSearchButton = new JButton("Search");
        frame.add(stockAboveSmaSearchButton, BorderLayout.SOUTH);
        // Create a text area to display the list
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);


        stockAboveSmaSearchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String [] tickets = {"aaaa", "bbbb", "cccc"};
                for (String item : tickets) {
                    textArea.append(item + "\n"); // append each item to the text area
                }
            }
        });
        // Add an ActionListener to the button
        showListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TickerReader tickerReader = new TickerReader();
                Calculator calculator = new VolumeCalculator();
                java.util.List<String> tickers = tickerReader.tickers();
                final double percent = 1.8;
                java.util.List<String> tickersMetCriteria = new ArrayList<>();
                for(String ticker : tickers) {
                    wrapper.getClient().reqHistoricalData(1000 + 10, new USStockContract(ticker), "", "200 D", "1 day", "TRADES", 1, 1, false, null);

                    try {
                       Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }



                    double averageVolume = calculator.calculate(wrapper.getList());
                    List<Bar> list = wrapper.getList();
                    if(list.size() <100){
                        System.err.println("ticker===" + ticker);
                        continue;
                    }
                    double lastVolume = list.get(list.size() - 1).volume().longValue();
                    if (lastVolume > percent * averageVolume) {
                        tickersMetCriteria.add(ticker);
                        // System.out.println("Last volume is ===============" + lastVolume);
                    }

                }

                textArea.setRows(tickersMetCriteria.size()+1);
                textArea.setText(""); // clear the text area
                for (String item : tickersMetCriteria) {
                    textArea.append(item + "\n"); // append each item to the text area
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
