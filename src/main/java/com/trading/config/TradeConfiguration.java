package com.trading.config;

public class TradeConfiguration {
    public double getRiskPercent() {
        return riskPercent;
    }

    public void setRiskPercent(double riskPercent) {
        this.riskPercent = riskPercent;
    }

    private double riskPercent ;

    public double getStopPercent() {
        return stopPercent;
    }

    public void setStopPercent(double stopPercent) {
        this.stopPercent = stopPercent;
    }

    private double stopPercent ;

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    private double capital;


}
