package com.trading.support;

import com.ib.client.Bar;

import java.util.List;

public interface Calculator {
    public void calculate(List<Bar> barList);
}
