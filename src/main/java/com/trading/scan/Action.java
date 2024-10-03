package com.trading.scan;

import com.ib.client.Bar;

import java.util.List;

public abstract class Action {
     abstract boolean  execute(List<Bar> list, String symbol);



}
