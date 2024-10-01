package com.trading.support.reader;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TickerReader {

    public List<String> tickers()  {
        Path path = null;
        InputStream inputStream;
        try {

             inputStream = getClass().getClassLoader().getResourceAsStream("nasdaqlisted.txt");

           // path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
             //       .getResource("nasdaqlisted.txt")).toURI());
            //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("nasdaqlisted.txt");

        }catch (Exception e) {
            System.err.println("Could not get path to nasdaqlisted.txt file" + path);
            return new ArrayList<>();
        }

        /*
        System.out.println("path====="+ path);
        Stream<String> lines = null;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            System.err.println("Could not get path to nasdaqlisted.txt file");
            return new ArrayList<>();
        }
        Stream<String> limited = lines.limit(50);
        List<String> tickers = new ArrayList<>();
        limited.forEach(line ->{
            tickers.add(line.split("\\|")[0]);
        });

         */
       List<String> tickers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.split("\\|")[0];
               // if(tickers.size()> 50) {
                 //   break;
                //}
                tickers.add(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tickers.remove(0);


        return tickers;

    }
}
