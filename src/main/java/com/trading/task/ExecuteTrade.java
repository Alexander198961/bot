package com.trading.task;

import com.trading.EWrapperImpl;
import com.trading.scan.CrossScan;
import com.trading.scan.PlaceOrderAction;
import com.trading.scan.Scan;

import java.util.Arrays;
import java.util.List;

public class ExecuteTrade {
    private final EWrapperImpl wrapper;

    public ExecuteTrade(EWrapperImpl wrapper) {
        this.wrapper = wrapper;
    }

    public void execute() {

        //Scan scan = new CrossScan(shortEmaValue,longEmaValue, bellowEmaPercent, largeEma);
        //List<String> list = scan.scan(wrapper, new PlaceOrderAction(wrapper, capital,riskPercent,  stopPercent), Arrays.asList(tickersArray));
    }
}
