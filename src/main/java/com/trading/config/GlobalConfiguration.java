package com.trading.config;

public class GlobalConfiguration {
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String host="localhost";

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port=7496;
}
