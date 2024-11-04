package com.trading.scan;
import java.util.*;

public class LongCondition extends Condition{
    public LongCondition(Double price, Double bellowEma, List<Double> largeEmaList, List<Double> smallEmaList) {
        super(price, bellowEma, largeEmaList, smallEmaList);
    }

    @Override
    public boolean isMeet() {
        if (smallEmaList.get(smallEmaList.size() - 1) > largeEmaList.get(largeEmaList.size() - 1) && ((ema200Value - ema200Value / 100 * bellowEma) < price)) {
            if(largeEmaList.get(largeEmaList.size()  - 2) > smallEmaList.get(smallEmaList.size()- 2)) {
                return true;
            }
            else{
                return false;
            }
        }
        return false;
        //return false;
    }
}
