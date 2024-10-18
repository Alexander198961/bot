package com.trading.scheduler;

import com.trading.EWrapperImpl;
import com.trading.cache.Cache;
import com.trading.config.EmaConfiguration;
import com.trading.config.RequestConfiguration;
import com.trading.config.TradeConfiguration;
import com.trading.scan.CrossScan;
import com.trading.scan.PlaceOrderAction;
import com.trading.scan.Scan;
import com.trading.scan.TickerEntry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    EWrapperImpl wrapper;

    public TaskScheduler(EWrapperImpl wrapper) {
        this.wrapper = wrapper;
    }

    public void run() {
        // Create a ScheduledExecutorService with a thread pool of size 1


        // Schedule a task to run every 5 seconds
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //Cache.cache.getIfPresent(Cache.Keys.StrategyEnabled.name());
                    if (Cache.cache.getIfPresent(Cache.Keys.EmaConfig.name()) != null && Cache.cache.getIfPresent(Cache.Keys.Tickers.name()) != null) {
                    System.out.println("inside: ");

                    try {

                        EmaConfiguration emaConfiguration = (EmaConfiguration) Cache.cache.getIfPresent(Cache.Keys.EmaConfig.name());
                        TradeConfiguration tradeConfiguration = (TradeConfiguration) Cache.cache.getIfPresent(Cache.Keys.TradeConfig.name());
                        List<String> tickers = (List<String>) Cache.cache.getIfPresent(Cache.Keys.Tickers.name());
                        Scan scanner = new CrossScan(emaConfiguration.getShortEmaValue(), emaConfiguration.getLongEmaValue(), emaConfiguration.getBellowEmaPercent(), emaConfiguration.getLargeEma());
                        List<String> list = scanner.scan(wrapper, new PlaceOrderAction(wrapper, tradeConfiguration.getCapital(), tradeConfiguration.getRiskPercent(), tradeConfiguration.getStopPercent(), tradeConfiguration.getTrailingStop()), tickers);
                    } catch (Exception e) {
                        System.out.println("exception=====" + e.getMessage());
                    }
                } else {


                    System.out.println("Task executed at: " + System.currentTimeMillis());
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
        System.out.println("Task ENDED: " + System.currentTimeMillis());
        // Initial delay is 0, and the task runs every 5 seconds
    }

    private void modifyTask(int periodInSeconds) {

       /*
        System.out.println("Scheduling task with period: " + periodInSeconds + " seconds");

        // Cancel the previous task if it exists
        if (scheduler != null && !scheduler) {
            scheduler.cancel(true);
        }

        // Schedule a new task
        scheduledTask = scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Task executed at: " + System.currentTimeMillis());
        }, 0, periodInSeconds, TimeUnit.SECONDS);

        */
    }

    public void stop() {
        scheduler.schedule(() -> scheduler.shutdown(), 1, TimeUnit.SECONDS);
    }
}
