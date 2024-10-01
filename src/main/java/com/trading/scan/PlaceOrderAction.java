package com.trading.scan;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.trading.EWrapperImpl;
import com.trading.api.USStockContract;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class PlaceOrderAction  implements Action{
    public PlaceOrderAction(EWrapperImpl eWrapperImpl) {
        this.wrapper = eWrapperImpl;


    }


    private final EWrapperImpl wrapper;

    @Override
    public boolean execute(List<Bar> list, String symbol) {
        double accountValue= 10000;
        int risk = 1;
        double amountToPut = accountValue/100 * risk;

        double price =list.get(list.size() -1).close();
        double totalQty = amountToPut/price;
        if(totalQty>0){
            totalQty = (int)totalQty;
        }
        double stopPrice= price - price/100* 4;
        Contract contract = new USStockContract(symbol);
        Order order = new Order();
       // wrapper.id
       // wrapper.nextValidId(orderId);
        int orderId = 0 ;
        wrapper.getClient().reqIds(1);
        orderId = wrapper.getCurrentOrderId();

       // orderId++;
        order.action("BUY");
        order.orderType("MKT");
        order.totalQuantity(Decimal.get(totalQty));
        wrapper.getClient().placeOrder(orderId, contract, order);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        wrapper.nextValidId(orderId);
        orderId=wrapper.getCurrentOrderId();
        //orderId++;
        order = new Order();
        order.action("SELL");
        order.orderType("LMT");
        order.totalQuantity(Decimal.get(totalQty));
        DecimalFormat df = new DecimalFormat("#.##");
        stopPrice = Double.parseDouble(df.format(stopPrice));
        order.lmtPrice(stopPrice);
        wrapper.getClient().placeOrder(orderId+1, contract, order);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
