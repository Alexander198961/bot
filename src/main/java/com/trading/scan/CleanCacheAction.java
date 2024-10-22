package com.trading.scan;

import com.ib.client.Bar;
import com.trading.cache.Cache;

import java.util.List;

public class CleanCacheAction extends Action{

    @Override
     public boolean execute(List<Bar> list, String ticker) {
        Cache.cache.invalidate(ticker);
        Cache.cache.invalidate(Cache.Keys.LastRun.name() + ticker);
        return false;
    }
}
