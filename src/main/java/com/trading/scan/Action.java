package com.trading.scan;

import com.ib.client.Bar;

import java.util.List;

public interface Action {
     boolean execute(List<Bar> list, String symbol);
}
