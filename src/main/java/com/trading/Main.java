package com.trading;

import com.ib.client.EClientSocket;
import com.trading.cache.Cache;
import com.trading.config.GlobalConfiguration;
import com.trading.data.TradeHistory;
import com.trading.gui.CommonForm;
import com.trading.gui.ConfigurationForm;
import com.trading.gui.HostPortInputDialog;
import com.trading.gui.MainForm;
import com.trading.scheduler.TaskScheduler;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

   // static TaskScheduler taskScheduler ;

    public static void main(String[] args) throws IOException {
        Cache cache = new Cache();
        EClientSocket m_client;
        EWrapperImpl wrapper;
        cache.init();
        List<TradeHistory> list = new ArrayList();
        Cache.cache.put(Cache.Keys.TradeHistory.name(), list);
        Cache.cache.put(Cache.Keys.StrategyEnabled.name(), true);
       // StockScarper stockScarper = new StockScarper();
       // List<String> sp500List = stockScarper.fetch("https://en.wikipedia.org/wiki/List_of_S%26P_500_companies",0);
        //List<String> dowStocks = stockScarper.fetch("https://en.wikipedia.org/wiki/Dow_Jones_Industrial_Average",1);
       // FtpDownloader ftpDownloader = new FtpDownloader();
       // TickerReader tickerReader = new TickerReader();
        //List<String> nasdaq = tickerReader.tickers(ftpDownloader.downloadToText());
        //Map<String, List<String>> indexTickerStorage = new HashMap<>();
        //sp500List =new ArrayList<>();
        //sp500List.add("BANL");
       // indexTickerStorage.put("SP500", sp500List);
       // indexTickerStorage.put("DOW", dowStocks);
        //indexTickerStorage.put("NASDAQ", nasdaq);
       // System.out.println("fetch==="+ sp500Scraper.fetch());
        /*
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        int port = Integer.parseInt(properties.getProperty("port"));
        String host =properties.getProperty("host");

         */
        //ConfigurationForm configurationForm = new ConfigurationForm();
       // configurationForm.show();
        //SwingUtilities.invokeLater()

        HostPortInputDialog dialog = new HostPortInputDialog(null);
        dialog.setVisible(true);

        System.out.println("dialog===="+dialog.getPort());
        System.out.println("dialog===="+dialog.getHost());
       // GlobalConfiguration globalConfiguration = new GlobalConfiguration();
       // String host= globalConfiguration.getHost();
       // int port=globalConfiguration.getPort();
        String host= dialog.getHost();
        Integer port = dialog.getPort();
        System.out.println("ib tws port="+ port);
        System.out.println("ib tws host="+ host);
        assert port>0;
        assert !host.isEmpty();
        wrapper = new EWrapperImpl();
        IBSignalHandler ibSignalHandler = new IBSignalHandler(wrapper, port, host);
        ibSignalHandler.run();
        m_client = wrapper.getClient();
        assert m_client.isConnected();
        if (m_client.isConnected()) {
            System.out.println("Connected to TWS");
        }else{
            System.out.println("Couldn't connect");
            System.exit(10);
        }


       // sp500Tickers = new ArrayList<>();
        //sp500Tickers.add("BCOV");

        //TableWithDropdownForm tableWithDropdownForm = new TableWithDropdownForm();
        //tableWithDropdownForm.display();
        MainForm mainForm = new MainForm();

        mainForm.display();
        JTextArea textArea = mainForm.getTextArea();
        TaskScheduler taskScheduler  = new TaskScheduler(wrapper, textArea, mainForm);
        taskScheduler.run();

    }

    private static void gracefulShutdown() {

    }
}





