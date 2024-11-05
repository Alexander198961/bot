package com.trading.scheduler;

import com.trading.EWrapperImpl;
import com.trading.api.USStockContract;
import com.trading.gui.MainForm;
import com.trading.scan.Utils;
import com.trading.storage.TickerStorage;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class TaskPositionUpdater extends Scheduler {
    private Utils utils = new Utils();
    public TaskPositionUpdater(EWrapperImpl wrapper, MainForm mainForm ) {
        this.wrapper = wrapper;
        this.mainForm = mainForm;
    }
    public void run(){
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {


                for ( String ticker: TickerStorage.executedTickerStorage.keySet()){
                    wrapper.setLastPrice(-1);
                 // wrapper.repn
                    wrapper.getClient().reqMktData(10222, new USStockContract(ticker), "", false, false, null);
                    utils.pause(500);
                    wrapper.getClient().cancelMktData(10222);
                    double price = wrapper.getLastPrice();
                    if(price < 0){
                        continue;
                    }
                    for(int i=0;i<5;i++) {
                      String currentTicker = (String) mainForm.getDefaultTableModel().getValueAt(i,0);
                      if(currentTicker.equals(ticker)){
                         double qty= (double) mainForm.getDefaultTableModel().getValueAt(i,5);
                         double averagePrice =  TickerStorage.executedTickerStorage.get(ticker);
                         double profitLose = (price - averagePrice)* qty;
                         mainForm.getDefaultTableModel().setValueAt(profitLose,i,7);

                      }
                    }
               }


            }
        }, 0, 60, TimeUnit.SECONDS);
    }
}
