package com.trading.tickers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockScarper {

    public List<String> fetch( String url, int index) throws IOException {
        //String url = "https://en.wikipedia.org/wiki/List_of_S%26P_500_companies";

        // Fetch the Wikipedia page
        Document doc = Jsoup.connect(url).get();

        // Select the table with the list of S&P 500 companies
        Element table = doc.select("table#constituents").first();
        Elements rows = table.select("tbody > tr");

        List<String> sp500Companies = new ArrayList<>();

        // Iterate over rows to extract the ticker symbols
        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.size() > index) {
                String ticker = cols.get(index).text();  // First column contains the ticker symbol
                sp500Companies.add(ticker);
            }
        }

        // Print the list of S&P 500 companies
        System.out.println("S&P 500 Ticker Symbols:");
        for (String company : sp500Companies) {
            System.out.println(company);
        }
    return sp500Companies;
    }
}