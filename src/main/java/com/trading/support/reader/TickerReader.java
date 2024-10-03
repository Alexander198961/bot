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

    public List<String> tickers(String result) {
        List<String> tickers = new ArrayList<>();
        for (String line : result.split("\\n")) {
            line = line.split("\\|")[0];
            tickers.add(line);
        }
        tickers.remove(0);
        return tickers;
    }
}
