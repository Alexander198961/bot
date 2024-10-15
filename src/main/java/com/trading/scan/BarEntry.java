package com.trading.scan;

public class BarEntry {
    public BarEntry(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    private String size;
}
