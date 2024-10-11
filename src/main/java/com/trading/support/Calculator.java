package com.trading.support;

import com.ib.client.Bar;

import java.util.List;
import java.util.Set;

public interface Calculator {
    public double calculate(List<Bar> barList);
}
