package com.trading.scan;

import com.ib.client.Bar;
import com.trading.EWrapperImpl;
import com.trading.api.USStockContract;
import com.trading.api.UnitController;
import com.trading.cache.Cache;
import com.trading.config.RequestConfiguration;

import java.time.Instant;
import java.util.*;

public abstract class Scan {
    private final UnitController unit = new UnitController();

    abstract boolean criteriaIsMeet(List<Bar> list);

    Scan() {
        unit.init();
    }

    private boolean isListValid(Set<Bar> list, String ticker) {
        if (list.size() < 3) {
            System.err.println("ticker===" + ticker);
            return false;
        }
        return true;
    }


    public List<String> scan(EWrapperImpl wrapper, Action action, List<String> tickers, RequestConfiguration requestConfiguration) {
        List<String> tickersMeetCriteria = new ArrayList<>();
        Utils utils = new Utils();
        SaveTickerAction saveTickerAction = new SaveTickerAction();
       /*
        if (action instanceof SaveTickerAction) {
            SaveTickerAction saveTickerAction = (SaveTickerAction) action;
            saveTickerAction.setTicker(tickersMeetCriteria);
        }

        */
        Map<String, Entry> mapTickersState = (Map) Cache.cache.getIfPresent(Cache.Keys.IsScheduled + Cache.Keys.tickersStateMap.name());
        for (String ticker : tickers) {
            if (ticker == null || ticker.isEmpty()) {
                continue;
            }

            if(mapTickersState == null || mapTickersState.get(ticker) == null) {
                continue;

            }
            if(!mapTickersState.get(ticker).getEnabled()){
                continue;
            }
            Map<String, BarEntry> tickersStateMap = (Map<String, BarEntry>) Cache.cache.getIfPresent(Cache.Keys.BarTimeFrame.name() + Cache.Keys.tickersStateMap.name());
            String barSize = tickersStateMap.get(ticker).getSize();
            // todo use cache ?
            System.out.println("test==="+Cache.cache.getIfPresent(ticker));
            if (Cache.cache.getIfPresent(ticker) == null) {
                wrapper.setList(new HashSet<>());
                if (!wrapper.getClient().isConnected()) {
                    System.err.println("is not connected");
                    break;
                }
                String period = unit.getPeriodMap().get(barSize);
                // long epochTimeLastRun =  System.currentTimeMillis();
                Long epochTimeLastRunSecond = Instant.now().getEpochSecond();

                //String barSize = requestConfiguration.getBarSize();
                assert period!= null;
                assert barSize!= null;
                wrapper.getClient().reqHistoricalData(1010, new USStockContract(ticker), "", period, barSize, "TRADES", 1, 1, false, null);
                utils.pause(1000);
                if (!utils.isConnected(wrapper)) {
                    tickersMeetCriteria.add("Not connected");
                }
                Set<Bar> list = wrapper.getList();
                if (!isListValid(list, ticker)) {
                    continue;
                }
                //Cache.cache.put("lastRun", epochTimeLastRunSecond);
                //Cache.cache.put(ticker, list);
                saveTickerAction.saveToCache(list, ticker,epochTimeLastRunSecond);
                if (check(list, ticker, action, tickersMeetCriteria) != null) {
                    return tickersMeetCriteria;
                }


            } else {
                Long epochTimeLastRunSecond = (Long) Cache.cache.getIfPresent(Cache.Keys.LastRun + ticker);
                Long barSizeSeconds = unit.getMapSeconds().get(barSize);
                long epochTimeCurrent = Instant.now().getEpochSecond();
                if (epochTimeCurrent > (epochTimeLastRunSecond + barSizeSeconds)) {
                    System.out.println("read from cache===");
                    Set<Bar> set = (Set<Bar>) Cache.cache.getIfPresent(ticker);
                    wrapper.setList(new HashSet<>());
                    String period = unit.getShortPeriodMap().get(barSize);
                    assert period!= null;
                    assert barSize!= null;
                    wrapper.getClient().reqHistoricalData(1010, new USStockContract(ticker), "", period, barSize, "TRADES", 1, 1, false, null);
                    utils.pause(1000);
                    set.addAll(wrapper.getList());
                    saveTickerAction.saveToCache(set,ticker, epochTimeCurrent);
                    //Cache.cache.put("lastRun", epochTimeCurrent);
                    //set.addAll(wrapper.getList());
                    //Cache.cache.put(ticker, set);
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

 // todo : maybe place multiple orders
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
        //else {
            //SaveTickerAction actionSave = new SaveTickerAction();
           // actionSave.saveToCache(new ArrayList<>(list), ticker);
        //}

        return null; // Or handle this case differently if needed
    }
}



