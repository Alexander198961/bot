package com.trading.scan;

import com.trading.EWrapperImpl;

public class Utils {
    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected(EWrapperImpl wrapper) {
       if( wrapper.getErrorCode() == 502 || wrapper.getErrorCode() == 503  || wrapper.getErrorCode() == 504 ) {
           return false;
       } ;
       return true;

    }
}
