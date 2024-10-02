package com.trading.scan;

import com.ib.client.Bar;
import com.ib.controller.Account;
import com.trading.EWrapperImpl;
import com.trading.api.USStockContract;
import com.trading.cache.Cache;
import com.trading.support.Calculator;
import com.trading.support.VolumeCalculator;
import com.trading.support.reader.TickerReader;

import java.util.ArrayList;
import java.util.List;

public abstract class Scan {

    abstract boolean criteriaIsMeet(List<Bar> list );

    public List<String> scan(EWrapperImpl wrapper, Action action, java.util.List<String> tickers ) {
        java.util.List<String> tickersMeetCriteria = new ArrayList<>();
       // TickerReader tickerReader = new TickerReader();
       // java.util.List<String> tickers = tickerReader.tickers();
       // tickers = new ArrayList<>();
       // tickers.add("AEIS");
        //tickers = new ArrayList<>();
        //tickers.add("ADSE");
        for (String ticker : tickers) {

            if (Cache.cache.getIfPresent(ticker)==null) {
                wrapper.setList(new ArrayList<>());
                if(!wrapper.getClient().isConnected()){
                    System.err.println("is not connected");
                    break;
                }
                wrapper.getClient().reqHistoricalData(1000 + 10, new USStockContract(ticker), "", "210 D", "1 day", "TRADES", 1, 1, false, null);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                List<Bar> list = wrapper.getList();
                if (list.size() < 100) {
                    System.err.println("ticker===" + ticker);
                    continue;
                }
                Cache.cache.put(ticker, list);
                if (criteriaIsMeet(list)) {
                    if(action.execute(list, ticker)){
                        tickersMeetCriteria.add(ticker);
                        return tickersMeetCriteria;
                    };
                    System.err.println("ticker met criteria===" + ticker);
                    tickersMeetCriteria.add(ticker);
                }



            }
            else {
                System.out.println("read from cache===");
                List<Bar> list = Cache.cache.getIfPresent(ticker);
                wrapper.setList(new ArrayList<>());
                if (list.size() < 100) {
                    System.err.println("ticker===" + ticker);
                    continue;
                }
                if (criteriaIsMeet(list)) {
                     if(action.execute(list, ticker)){
                         tickersMeetCriteria.add(ticker);
                         return tickersMeetCriteria;
                     };
                    System.err.println("ticker met criteria===" + ticker);
                    tickersMeetCriteria.add(ticker);
                }
            }
        }
        return tickersMeetCriteria;
    }
}
