package com.trading;

import com.ib.client.EClientSocket;
import com.trading.cache.Cache;
import com.trading.config.GlobalConfiguration;
import com.trading.gui.MainForm;
import com.trading.scheduler.TaskScheduler;

import java.io.IOException;

public class Main {

   // static TaskScheduler taskScheduler ;

    public static void main(String[] args) throws IOException {
        Cache cache = new Cache();
        EClientSocket m_client;
        EWrapperImpl wrapper;
        cache.init();
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
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        String host= globalConfiguration.getHost();
        int port=globalConfiguration.getPort();
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
        TaskScheduler taskScheduler  = new TaskScheduler(wrapper);
        taskScheduler.run();

       // sp500Tickers = new ArrayList<>();
        //sp500Tickers.add("BCOV");

        //TableWithDropdownForm tableWithDropdownForm = new TableWithDropdownForm();
        //tableWithDropdownForm.display();
        MainForm mainForm = new MainForm();
        mainForm.display();



    }

    private static void gracefulShutdown() {

    }
}





