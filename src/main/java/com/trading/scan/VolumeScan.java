package com.trading.scan;

import com.ib.client.Bar;
import com.trading.support.Calculator;
import com.trading.support.VolumeCalculator;

import java.util.List;

public class VolumeScan  extends Scan {
    @Override
    boolean criteriaIsMeet(List<Bar> list) {
        final double percent = 1.8;
        Calculator calculator = new VolumeCalculator();
        double averageVolume = calculator.calculate(list);
        double lastVolume = list.get(list.size() - 1).volume().longValue();
        // System.out.println("Last volume is ===============" + lastVolume);
        return lastVolume > percent * averageVolume;
    }
}
