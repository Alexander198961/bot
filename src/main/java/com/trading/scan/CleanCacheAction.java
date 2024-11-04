package com.trading.scan;

import com.ib.client.Bar;
import com.trading.cache.Cache;
import com.trading.storage.TickerStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanCacheAction extends Action{

    @Override
     public boolean execute(List<Bar> list, String ticker) {
        Cache.cache.invalidate(ticker);
        Cache.cache.invalidate(Cache.Keys.LastRun.name() + ticker);
        TickerStorage.executedTickerStorage.remove(ticker);
        return false;
    }
}
