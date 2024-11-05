package com.trading.scan;

import com.ib.client.Bar;
import com.trading.EWrapperImpl;
import com.trading.api.CustomBar;
import com.trading.api.USStockContract;
import com.trading.api.UnitController;
import com.trading.cache.Cache;
import com.trading.config.BarStateExecution;
import com.trading.gui.MainForm;
import com.trading.storage.TickerStorage;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Scan {
    private final UnitController unit = new UnitController();

    abstract boolean criteriaIsMeet(List<Bar> list);

    Scan() {
        unit.init();
    }

    private boolean isListValid(List<CustomBar> list, String ticker) {
        if (list.size() < 3) {
            System.err.println("ticker===" + ticker);
            return false;
        }
        return true;
    }


    public List<String> scan(EWrapperImpl wrapper, Action action, List<String> tickers , MainForm mainForm) {
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
        Set<String> executedTickers = TickerStorage.executedTickerStorage.keySet();
        for (String ticker : tickers) {
            i++;

            if(executedTickers.contains(ticker)){
                continue;
            }


            if (ticker == null || ticker.isEmpty()) {
                continue;
            }
            if(Cache.cache.getIfPresent(Cache.Keys.IsScheduled + i.toString()) == null){
                continue;
            }
            else{
             Entry<Boolean> entry = (Entry<Boolean>) Cache.cache.getIfPresent(Cache.Keys.IsScheduled.name() + i);
             if (entry.getEntry() == false) {
                 continue;
             }
            }


            Entry<String> entry = (Entry<String>) Cache.cache.getIfPresent(Cache.Keys.BarTimeFrame.name() + i);
            String barSize = entry.getEntry();
            Cache.cache.put(ticker+ Cache.Keys.RowNumber, i);
           // String barSize = "1 day";
          //  String barSize = "15 mins";
           // String barSize = "1 min";
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
                List<CustomBar> list = wrapper.getList();
                if (!isListValid(list, ticker)) {
                    continue;
                }
                //Cache.cache.put("lastRun", epochTimeLastRunSecond);
                //Cache.cache.put(ticker, list);
                //Cache.cache.put(ticker, list);
                saveTickerAction.saveToCache(list, ticker,epochTimeLastRunSecond);

                if (check(list, ticker, action, tickersMeetCriteria, mainForm, unit.getMapSeconds().get(barSize)) != null) {
                   // return tickersMeetCriteria;
                }


            } else {
                Long epochTimeLastRunSecond = (Long) Cache.cache.getIfPresent(Cache.Keys.LastRun + ticker);
                Long barSizeSeconds = unit.getMapSeconds().get(barSize);
                long epochTimeCurrent = Instant.now().getEpochSecond();
                if(epochTimeLastRunSecond == null){
                    continue;
                }
                if (epochTimeCurrent > (epochTimeLastRunSecond + barSizeSeconds)) {
                    System.out.println("read from cache===");
                    List<CustomBar> list = (List<CustomBar>) Cache.cache.getIfPresent(ticker);
                    assert list != null;
                    if(list.size()> 3000){
                        list.subList(0,2000).clear();
                    }
                    String period = unit.getShortPeriodMap().get(barSize);
                    assert period!= null;
                    wrapper.getClient().reqHistoricalData(1010, new USStockContract(ticker), "", period, barSize, "TRADES", 1, 1, false, null);
                    utils.pause(1000);
                    List<CustomBar> addedlist = wrapper.getList();
                    for (CustomBar bar : addedlist) {
                        if(!list.contains(bar)) {
                            System.out.println("addedlist==="+ list.get(list.size()-1).time());
                            System.out.println(bar.time());
                            list.add(bar);
                        }
                    }
                   // set.addAll(wrapper.getList());
                    saveTickerAction.saveToCache(list,ticker, epochTimeCurrent);
                    //Cache.cache.put("lastRun", epochTimeCurrent);
                    //set.addAll(wrapper.getList());
                    //Cache.cache.put(ticker, set);
                    if (check(list, ticker, action, tickersMeetCriteria, mainForm, barSizeSeconds) != null) {

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
    public Collection<String> check(List<CustomBar> list, String ticker, Action action, List<String> tickersMeetCriteria, MainForm mainForm, Long barSize) {

       //  fix

        if (criteriaIsMeet(new ArrayList<>(list))) {
            CleanCacheAction cleanCacheAction = new CleanCacheAction();
            cleanCacheAction.execute(null, ticker);


            int j = 0;
            do {
                String tickerRow = (String) mainForm.getDefaultTableModel().getValueAt(j, 0);
                if (tickerRow != null && tickerRow.equals(ticker)) {
                    break;
                }
                j=j+1;
            } while (j < 5);
            //mainForm.getDefaultTableModel().getValueAt(0,4);
            System.out.println("test===="  + mainForm.getDefaultTableModel().getValueAt(0,4));
           // Cache.cache.put(Cache.Keys.BarStateExecution.name() + new Integer(i), BarStateExecution.OPEN);
            Entry<Boolean> barStateExecution = (Entry<Boolean>) Cache.cache.getIfPresent(Cache.Keys.BarStateExecution.name() + new Integer(j));

           // String barStateExecution = (String) mainForm.getDefaultTableModel().getValueAt(j,4);
          //  Entry<String> entry = (Entry<String>) Cache.cache.getIfPresent(Cache.Keys.BarStateExecution.name() + j);
           // String barStateExecution = entry.getEntry();




            //todo remove
            //String barStateExecution = String.valueOf(BarStateExecution.CLOSED);
            SaveTickerAction actionSave = new SaveTickerAction();
            actionSave.setTicker(tickersMeetCriteria);
            actionSave.execute(new ArrayList<>(list), ticker);
            boolean isFalse = true;

            if (barStateExecution.getEntry() instanceof Boolean && barStateExecution.getEntry()  ) {
                // Cache.Keys.BarStateExecution.name()
                if (action.execute(new ArrayList<>(list), ticker)) {

                    return tickersMeetCriteria;
                }
                System.out.println("ticker met criteria===" + ticker);
                //tickersMeetCriteria.add(ticker);
            } else {
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> {
                    action.execute(new ArrayList<>(list), ticker);
                }, barSize, TimeUnit.SECONDS);
            }
        }
            return tickersMeetCriteria;

        }

}



