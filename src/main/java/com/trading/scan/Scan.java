package com.trading.scan;

import com.ib.client.Bar;
import com.trading.EWrapperImpl;
import com.trading.api.USStockContract;
import com.trading.api.UnitController;
import com.trading.cache.Cache;
import com.trading.config.RequestConfiguration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Scan {
    private final UnitController unit = new UnitController();
    abstract boolean criteriaIsMeet(List<Bar> list );
    Scan(){
        unit.init();
    }
    private  boolean isListValid(Set<Bar> list, String ticker){
        if (list.size() < 100) {
            System.err.println("ticker===" + ticker);
           return true;
        }
        return false;
    }


    public List<String> scan(EWrapperImpl wrapper, Action action, List<String> tickers , RequestConfiguration requestConfiguration) {
        List<String> tickersMeetCriteria = new ArrayList<>();
        Utils utils = new Utils();
        if (action instanceof SaveTickerAction){
            SaveTickerAction saveTickerAction = (SaveTickerAction) action;
            saveTickerAction.setTicker(tickersMeetCriteria);
        }
        for (String ticker : tickers) {
            if (ticker == null || ticker.isEmpty()) {
                continue;
            }

            // todo use cache ?
            if (Cache.cache.getIfPresent(ticker) == null) {
                wrapper.setList(new HashSet<>());
                if (!wrapper.getClient().isConnected()) {
                    System.err.println("is not connected");
                    break;
                }
                String period = unit.getPeriodMap().get(requestConfiguration.getBarSize());
               // long epochTimeLastRun =  System.currentTimeMillis();
               Long epochTimeLastRunSecond = Instant.now().getEpochSecond();
                String barSize = requestConfiguration.getBarSize();
                wrapper.getClient().reqHistoricalData(1000 + 10, new USStockContract(ticker), "", period, requestConfiguration.getBarSize(), "TRADES", 1, 1, false, null);
                utils.pause(1000);
                if (!utils.isConnected(wrapper)) {
                    tickersMeetCriteria.add("Not connected");
                }
                Set<Bar> list = wrapper.getList();
                if (isListValid(list, ticker)) {
                    continue;
                }
                Cache.cache.put("lastRun", epochTimeLastRunSecond);
                Cache.cache.put(ticker, list);
                if (check(list, ticker, action, tickersMeetCriteria) != null) {
                    return tickersMeetCriteria;
                }



            } else {
                Long epochTimeLastRunSecond = (Long) Cache.cache.getIfPresent("lastRun");
                Long barSizeSeconds = unit.getMapSeconds().get(requestConfiguration.getBarSize());
                long epochTimeCurrent = Instant.now().getEpochSecond();
                if(epochTimeCurrent >(epochTimeLastRunSecond +  barSizeSeconds)) {
                    System.out.println("read from cache===");
                    Set<Bar> set = (Set<Bar>) Cache.cache.getIfPresent(ticker);
                    wrapper.setList(new HashSet<>());
                    wrapper.getClient().reqHistoricalData(1000 + 10, new USStockContract(ticker), "", unit.getShortPeriodMap().get(requestConfiguration.getBarSize()), requestConfiguration.getBarSize(), "TRADES", 1, 1, false, null);
                    Cache.cache.put("lastRun", epochTimeCurrent);
                    set.addAll(wrapper.getList());
                    Cache.cache.put(ticker, set);
                    if (check(set, ticker, action, tickersMeetCriteria) != null) {
                        return tickersMeetCriteria;
                    }
                }


            }
        }
        if (tickersMeetCriteria.isEmpty()) {
            tickersMeetCriteria.add("No tickers are found");
        }

        return tickersMeetCriteria;
        }


    public Set<String> check(Set<Bar> list, String ticker, Action action, List<String> tickersMeetCriteria) {
        if (isListValid(list, ticker)) {
            return null; // Or handle this case differently if needed
        }
        if (criteriaIsMeet(new ArrayList<>(list))) {
            if (action.execute(new ArrayList<>(list), ticker)) {
                SaveTickerAction actionSave = new SaveTickerAction();
                actionSave.setTicker(tickersMeetCriteria);
                actionSave.execute(new ArrayList<>(list), ticker);
                return (Set<String>) tickersMeetCriteria;
            }

            System.out.println("ticker met criteria===" + ticker);
            //tickersMeetCriteria.add(ticker);
        }
        else{
            SaveTickerAction actionSave = new SaveTickerAction();
            actionSave.saveToCache(new ArrayList<>(list), ticker);
        }

        return null; // Or handle this case differently if needed
    }
}



