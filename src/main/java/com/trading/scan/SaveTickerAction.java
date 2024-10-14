package com.trading.scan;

import com.google.common.base.Ticker;
import com.ib.client.Bar;
import com.trading.cache.Cache;

import java.util.Collection;
import java.util.List;

public class SaveTickerAction extends Action {
    public List<String> getTicker() {
        return tickerList;
    }

    public void setTicker(List<String> tickerList) {
        this.tickerList = tickerList;
    }

    private List<String> tickerList;

    @Override
    public boolean execute(List<Bar> list, String ticker) {
        Cache.cache.put(ticker, list);
        tickerList.add(ticker);
        return false;
    }

    public void saveToCache(Collection<Bar> list, String ticker, Long epochTimeCurrent) {
        Cache.cache.put(Cache.Keys.LastRun.name() + ticker, epochTimeCurrent);
        Cache.cache.put(ticker, list);
    }


}
