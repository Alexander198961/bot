package com.trading.support;

import com.ib.client.Bar;

import java.util.List;
import java.util.Set;

public class VolumeCalculator implements Calculator{
    @Override
    public double calculate(List<Bar> barList) {
        double sum = 0;
        for (Bar bar : barList) {
           sum=sum+ bar.volume().value().doubleValue();
           System.out.println(sum);
        }
        double avg = sum/barList.size();
        System.out.println("Average volume: "+avg);

        return avg;

    }
}
