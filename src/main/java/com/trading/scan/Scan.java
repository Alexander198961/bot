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

    private boolean isListValid(Collection<Bar> list, String ticker) {
        if (list.size() < 3) {
            System.err.println("ticker===" + ticker);
            return false;
        }
        return true;
    }


    public List<String> scan(EWrapperImpl wrapper, Action action, List<String> tickers) {
        List<String> tickersMeetCriteria = new ArrayList<>();
        Utils utils = new Utils();
        SaveTickerAction saveTickerAction = new SaveTickerAction();
       /*
        if (action instanceof SaveTickerAction) {
            SaveTickerAction saveTickerAction = (SaveTickerAction) action;
            saveTickerAction.setTicker(tickersMeetCriteria);
        }

        */
       // Map<String, Entry> mapTickersState = (Map) Cache.cache.getIfPresent(Cache.Keys.IsScheduled + Cache.Keys.tickersStateMap.name());

       // Cache.cache.getIfPresent(Cache.Keys.IsScheduled )
        Integer i=-1;
        for (String ticker : tickers) {
            i++;


            if (ticker == null || ticker.isEmpty()) {
                continue;
            }

            if(Cache.cache.getIfPresent(Cache.Keys.IsScheduled + i.toString()) == null){
                continue;
            }
            else{
             Entry<Boolean> entry = (Entry<Boolean>) Cache.cache.getIfPresent(Cache.Keys.IsScheduled.name() + i.toString());
             if (entry.getEntry() == false) {
                 continue;
             }
            }

            Entry<String> entry = (Entry<String>) Cache.cache.getIfPresent(Cache.Keys.BarTimeFrame.name() + i.toString());
            String barSize = entry.getEntry();


           // Cache.cache.put(ticker+ Cache.Keys.RowNumber, i);
           // String barSize = "1 day";
           // todo temp disabled bug !!!!




             //
            wrapper.setList(new ArrayList<>());
           // String barSize = "1 day";
            // todo use cache ?
            if (Cache.cache.getIfPresent(ticker) == null) {
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
                List<Bar> list = wrapper.getList();
                if (!isListValid(list, ticker)) {
                    continue;
                }
                //Cache.cache.put("lastRun", epochTimeLastRunSecond);
                //Cache.cache.put(ticker, list);
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
                    List<Bar> list = (List<Bar>) Cache.cache.getIfPresent(ticker);
                    assert list != null;
                    if(list.size()> 3000){
                        list.subList(0,2000).clear();
                    }
                    String period = unit.getShortPeriodMap().get(barSize);
                    assert period!= null;
                    wrapper.getClient().reqHistoricalData(1010, new USStockContract(ticker), "", period, barSize, "TRADES", 1, 1, false, null);
                    utils.pause(1000);
                    assert list != null;
                    List<Bar> addedlist = wrapper.getList();
                    for (Bar bar : addedlist) {
                        if(!list.contains(bar)) {
                            list.add(bar);
                        }
                    }
                   // set.addAll(wrapper.getList());
                    saveTickerAction.saveToCache(list,ticker, epochTimeCurrent);
                    //Cache.cache.put("lastRun", epochTimeCurrent);
                    //set.addAll(wrapper.getList());
                    //Cache.cache.put(ticker, set);
                    if (check(list, ticker, action, tickersMeetCriteria) != null) {
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
    public Collection<String> check(Collection<Bar> list, String ticker, Action action, List<String> tickersMeetCriteria) {

        if (criteriaIsMeet(new ArrayList<>(list))) {
            if (action.execute(new ArrayList<>(list), ticker)) {
                SaveTickerAction actionSave = new SaveTickerAction();
                actionSave.setTicker(tickersMeetCriteria);
                actionSave.execute(new ArrayList<>(list), ticker);
                return tickersMeetCriteria;
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



