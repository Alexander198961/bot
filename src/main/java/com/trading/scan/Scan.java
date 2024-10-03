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

    private  boolean isListValid(List<Bar> list, String ticker){
        if (list.size() < 100) {
            System.err.println("ticker===" + ticker);
           return true;
        }
        return false;
    }


    public List<String> scan(EWrapperImpl wrapper, Action action, List<String> tickers ) {


        List<String> tickersMeetCriteria = new ArrayList<>();
        Utils utils = new Utils();
        if (action instanceof SaveTickerAction){
            SaveTickerAction saveTickerAction = (SaveTickerAction) action;
            saveTickerAction.setTicker(tickersMeetCriteria);
        }
        for (String ticker : tickers)


            if (Cache.cache.getIfPresent(ticker)==null) {
                wrapper.setList(new ArrayList<>());
                if(!wrapper.getClient().isConnected()){
                    System.err.println("is not connected");
                    break;
                }
                wrapper.getClient().reqHistoricalData(1000 + 10, new USStockContract(ticker), "", "210 D", "1 day", "TRADES", 1, 1, false, null);
                utils.pause(1000);
                if(!utils.isConnected(wrapper)){
                    tickersMeetCriteria.add("Not connected");
                }
                List<Bar> list = wrapper.getList();
                if (isListValid(list, ticker)) {
                    continue;
                }
                if(check(list,ticker,action, tickersMeetCriteria)!= null){
                    return tickersMeetCriteria;
                }




            }
            else {
                System.out.println("read from cache===");
                List<Bar> list = Cache.cache.getIfPresent(ticker);
                wrapper.setList(new ArrayList<>());
                if(check(list,ticker,action, tickersMeetCriteria)!= null){
                     return tickersMeetCriteria;
                 }

            }
        if (tickersMeetCriteria.isEmpty()) {
            tickersMeetCriteria.add("No tickers are found");
        }

        return tickersMeetCriteria;
        }

    public List<String> check(List<Bar> list, String ticker, Action action, List<String> tickersMeetCriteria) {
        if (isListValid(list, ticker)) {
            return null; // Or handle this case differently if needed
        }
        if (criteriaIsMeet(list)) {
            if (action.execute(list, ticker)) {
                SaveTickerAction actionSave = new SaveTickerAction();
                actionSave.setTicker(tickersMeetCriteria);
                actionSave.execute(list, ticker);
                return tickersMeetCriteria;
            }

            System.out.println("ticker met criteria===" + ticker);
            //tickersMeetCriteria.add(ticker);
        }
        else{
            SaveTickerAction actionSave = new SaveTickerAction();
            actionSave.saveToCache(list, ticker);
        }

        return null; // Or handle this case differently if needed
    }
}



