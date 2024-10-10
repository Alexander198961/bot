package com.trading.scan;

import com.ib.client.Bar;
import com.trading.support.EMACalculator;

import java.util.List;

public class CrossScan extends Scan {

    private double bellowEma = 0.0;

    public CrossScan(int SHORT, int LONG, double bellowEma, int LARGE_EMA) {
        this.SHORT = SHORT;
        this.LONG = LONG;
        this.bellowEma = bellowEma;
        this.LARGE_EMA =LARGE_EMA;

    }

    private  int SHORT;
    private  int LONG;
    private  int LARGE_EMA;

    @Override
    boolean criteriaIsMeet(List<Bar> list) {
        // int SHORT = 12;
        // int LONG = 26;
        //LARGE_EMA = 200;
        EMACalculator calculator = new EMACalculator();
        List<Double> smallEmaList = calculator.calculateEMA(list, SHORT);
        List<Double> largeEmaList = calculator.calculateEMA(list, LONG);
        List<Double> ema200List = calculator.calculateEMA(list, LARGE_EMA);
        int smallEmaSize = smallEmaList.size();
        int largeEmaSize = largeEmaList.size();
        int ema200Size = ema200List.size();
        if (ema200List.isEmpty()) {
            System.err.println("ema200List size is less than 1");
            return false;
        }
        double ema200Value = ema200List.get(ema200Size - 1);
        double price = list.get(list.size() - 1).close();
        if (smallEmaList.get(smallEmaSize - 1) > largeEmaList.get(largeEmaSize - 1) && ((ema200Value - ema200Value / 100 * bellowEma) < price)) {
            return largeEmaList.get(largeEmaSize - 2) > smallEmaList.get(smallEmaSize - 2);
        }
        return false;


    }
}
