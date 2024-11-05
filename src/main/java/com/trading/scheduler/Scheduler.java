package com.trading.scheduler;

import com.trading.EWrapperImpl;
import com.trading.cache.Cache;
import com.trading.config.EmaConfiguration;
import com.trading.config.TradeConfiguration;
import com.trading.gui.MainForm;
import com.trading.scan.CrossScan;
import com.trading.scan.PlaceOrderAction;
import com.trading.scan.Scan;
import com.trading.storage.TickerStorage;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Scheduler {
    protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    protected EWrapperImpl wrapper;
    protected JTextArea textArea;
    protected MainForm mainForm;

    public void stop() {
        scheduler.schedule(() -> scheduler.shutdown(), 1, TimeUnit.SECONDS);
    }


     public void run(){

    }
}
