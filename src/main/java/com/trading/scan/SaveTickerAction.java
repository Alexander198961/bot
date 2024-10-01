package com.trading.scan;

import com.ib.client.Bar;

import java.util.List;

public class SaveTickerAction implements Action {
    @Override
    public boolean execute(List<Bar> list, String symbol) {
        return false;
    }
}
