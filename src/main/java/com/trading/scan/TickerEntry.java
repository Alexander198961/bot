package com.trading.scan;

public class TickerEntry {
    public Boolean getEnabled() {
        return isEnabled;
    }

    private final Boolean isEnabled;

    public Boolean getTrailedStopEnabled() {
        return isTrailedStopEnabled;
    }

    public void setTrailedStopEnabled(boolean isTrailedStopEnabled) {
         this.isTrailedStopEnabled = isTrailedStopEnabled;
    }

    private Boolean isTrailedStopEnabled;



    public TickerEntry(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
