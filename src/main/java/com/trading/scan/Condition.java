package com.trading.scan;

import java.util.*;

public class Condition {
    public Condition(Double price, Double bellowEma,List<Double> largeEmaList, List<Double> smallEmaList) {
        this.price = price;
        this.bellowEma = bellowEma;
        this.largeEmaList = largeEmaList;
    }

    protected Double ema200Value;
    protected Double bellowEma;
    protected Double price;
    protected List<Double> largeEmaList;
    protected List<Double> smallEmaList;

    public boolean isMeet() {
        return false;
    }
}
