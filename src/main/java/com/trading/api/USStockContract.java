package com.trading.api;

import com.ib.client.Contract;

public class USStockContract extends Contract {
    public USStockContract(String symbol)
    {
        super.symbol(symbol);
        super.secType("STK");
        super.exchange("SMART");
        super.primaryExch("ISLAND");
        super.currency("USD");
    }
}
