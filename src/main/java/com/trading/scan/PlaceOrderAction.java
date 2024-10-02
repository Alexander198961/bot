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
    private double accountValue=10000;
    private double risk=1 ;
    public PlaceOrderAction(EWrapperImpl eWrapperImpl, double accountValue, double risk) {
        this.wrapper = eWrapperImpl;
        this.accountValue = accountValue;
        this.risk = risk;


    }


    private final EWrapperImpl wrapper;

    @Override
    public boolean execute(List<Bar> list, String symbol) {
        double accountValue= this.accountValue;
        double risk = this.risk;
        Contract contract = new USStockContract(symbol);
        wrapper.getClient().reqMktData(1000 + 44, contract, "", false, false, null);
        pause(500);
        double amountToPut = accountValue/100 * risk;
        DecimalFormat df = new DecimalFormat("#.##");
        //double price =list.get(list.size() -1).close();
        double price = wrapper.getLastPrice();
        if(price ==0 ){
            price = list.get(list.size() -1).close();
        }
        //todo :get currentPrice
        double totalQty = amountToPut/price;
        wrapper.setErrorCode(0);
        if(totalQty>0){
            totalQty = (int)totalQty;
        }
        else{
            // todo fix it
            System.out.println("Shares price is higher than amount could be allocated");
            return false;
            //totalQty = Double.parseDouble(df1.format( amountToPut/price));
        }
        if (totalQty <1){
            System.out.println("Shares price is higher than amount could be allocated");
            return false;
        }
        double stopPrice= price - price/100* 4;

        Order order = new Order();
        int orderId = 0 ;
        wrapper.getClient().reqIds(1);

        orderId = getOrderId();
        order.action("BUY");
        order.orderType("MKT");
        order.totalQuantity(Decimal.get(totalQty));
        wrapper.getClient().placeOrder(orderId, contract, order);
        pause(1000);
        if (wrapper.getErrorCode()>0){
            System.out.println("PlaceOrderAction: error code: "+wrapper.getErrorCode());
            return false;
        }
        wrapper.nextValidId(orderId);
        orderId = getOrderId();
        order = new Order();
        order.action("SELL");
        order.orderType("LMT");
        order.totalQuantity(Decimal.get(totalQty));
        stopPrice = Double.parseDouble(df.format(stopPrice));
        order.lmtPrice(stopPrice);
        wrapper.getClient().placeOrder(orderId+1, contract, order);
        pause(1000);
        return true;
    }

    private  void pause(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer getOrderId(){
        Integer orderId = wrapper.getCurrentOrderId();
        orderId++;
        return orderId;
    }
    private void placeOrder(){


    }
}
