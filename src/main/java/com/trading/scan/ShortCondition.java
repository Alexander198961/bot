package com.trading.scan;

import java.util.List;

public class ShortCondition extends Condition {
    public ShortCondition(Double price, Double bellowEma, List<Double> largeEmaList, List<Double> smallEmaList) {
        super(price, bellowEma, largeEmaList, smallEmaList);
    }

    @Override
    public boolean isMeet() {
        return false;
    }
}
