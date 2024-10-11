package com.trading.scan;

import com.ib.client.Bar;
import com.trading.support.EMACalculator;

import java.util.List;
import java.util.Set;

public class SmaScan extends Scan {
    @Override
    boolean criteriaIsMeet(List<Bar> list) {
        EMACalculator smaCalculator = new EMACalculator();
        double smaLastValue= smaCalculator.calculate(list, 200);
        double lastClosePrice = list.get(list.size() -1).close();
        return lastClosePrice > smaLastValue;
    }
}
