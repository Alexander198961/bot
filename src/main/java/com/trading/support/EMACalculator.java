package com.trading.support;

import com.ib.client.Bar;

import java.util.List;

public class EMACalculator implements Calculator {
    @Override
    public void calculate(List<Bar> barList) {

    }
    public Double calculate(List<Bar> barList, int period) {
        // Check if barList is smaller than the period
        if (barList.size() < period) {
            System.out.println("Not enough data to calculate SMA.");
            return (double) 0;
        }

        // Loop through the list and calculate the SMA for each window of `period` size
        for (int i = 0; i <= barList.size() - period; i++) {
            double sum = 0.0;
            // Sum the prices for the current window
            for (int j = i; j < i + period; j++) {
                sum += barList.get(j).close(); // Assuming `getClose()` returns the closing price of the Bar
            }
            // Calculate the average
            double sma = sum / period;
            System.out.println("SMA for day " + (i + period) + ": " + sma);
            return sma;

        }
        return (double) 0;
    }

    public double calculateEMA(List<Bar> barList, int period) {
        // Check if barList is smaller than the period
        /*
        if (barList.size() < period) {
            System.out.println("Not enough data to calculate EMA.");
            return;
        }

        double alpha = 2.0 / (period + 1);
        double previousEMA = 0.0;

        // Step 1: Calculate SMA for the first period as the initial EMA
        double sum = 0.0;
        for (int i = 0; i < period; i++) {
            sum += barList.get(i).close(); // Assuming `getClose()` returns the closing price of the Bar
        }
        double sma = sum / period;
         */
        //double sma = sum / period;
        double alpha = 2.0 / (period + 1);
        double previousEMA = 0.0;
        double sma = calculate(barList,period);
        previousEMA = sma; // The first EMA value is the SMA



        // Step 2: Calculate EMA for the remaining days
        for (int i = period; i < barList.size(); i++) {
            double currentPrice = barList.get(i).close();
            double currentEMA = (currentPrice * alpha) + (previousEMA * (1 - alpha));
            System.out.println("EMA for day " + (i + 1) + ": " + currentEMA);
            previousEMA = currentEMA; // Update the previous EMA for the next iteration
        }
        System.out.println("EMA for day " + period + ": " + previousEMA);
        return previousEMA;
    }
}
