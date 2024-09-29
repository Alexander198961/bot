package com.trading.support.reader;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TickerReader {

    public List<String> tickers()  {
        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource("nasdaqlisted.txt")).toURI());
        }catch (URISyntaxException e) {
            System.err.println("Could not get path to nasdaqlisted.txt file");
            return new ArrayList<>();
        }
        Stream<String> lines = null;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            System.err.println("Could not get path to nasdaqlisted.txt file");
            return new ArrayList<>();
        }
        Stream<String> limited = lines.limit(20);
        List<String> tickers = new ArrayList<>();
        limited.forEach(line ->{
            tickers.add(line.split("\\|")[0]);
        });
        tickers.remove(0);
        return tickers;

    }
}
