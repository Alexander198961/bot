package com.trading.config;

public class EmaConfiguration {
    public int getShortEmaValue() {
        return shortEmaValue;
    }

    public void setShortEmaValue(int shortEmaValue) {
        this.shortEmaValue = shortEmaValue;
    }

    private int shortEmaValue;

    public int getLongEmaValue() {
        return longEmaValue;
    }

    public void setLongEmaValue(int longEmaValue) {
        this.longEmaValue = longEmaValue;
    }

    private int longEmaValue;

    public int getLargeEma() {
        return largeEma;
    }

    public void setLargeEma(int largeEma) {
        this.largeEma = largeEma;
    }

    private int largeEma;

    public double getBellowEmaPercent() {
        return bellowEmaPercent;
    }

    public void setBellowEmaPercent(double bellowEmaPercent) {
        this.bellowEmaPercent = bellowEmaPercent;
    }

    private double bellowEmaPercent;
}
