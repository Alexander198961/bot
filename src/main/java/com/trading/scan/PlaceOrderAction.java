package com.trading.scan;

import com.ib.client.*;
import com.trading.EWrapperImpl;
import com.trading.api.USStockContract;

import java.text.DecimalFormat;
import java.util.List;

public class PlaceOrderAction extends Action {
    private double accountValue = 10000;
    private double risk = 1;
    private final double stopPercent;

    public PlaceOrderAction(EWrapperImpl eWrapperImpl, double accountValue, double risk, double stopPercent) {
        this.wrapper = eWrapperImpl;
        this.accountValue = accountValue;
        this.risk = risk;
        this.stopPercent = stopPercent;


    }

    ;

    private final EWrapperImpl wrapper;

    @Override
    public boolean execute(List<Bar> list, String symbol) {
        Utils utils = new Utils();
        int orderId = 0;
        double accountValue = this.accountValue;
        double risk = this.risk;
        wrapper.getClient().reqIds(1);
        wrapper.setLastPrice(0);
        orderId = getOrderId();
        Contract contract = new USStockContract(symbol);
        wrapper.getClient().reqMktData(orderId, contract, "", false, false, null);
        utils.pause(1000);
        double amountToPut = accountValue / 100 * risk;
        DecimalFormat df = new DecimalFormat("#.##");
        //double price =list.get(list.size() -1).close();
        double price = wrapper.getLastPrice();
        if (price == 0) {
            return false;
            //price = list.get(list.size() -1).close();
        }
        //todo :get currentPrice
        double totalQty = amountToPut / price;
        wrapper.setErrorCode(0);
        if (totalQty > 0) {
            totalQty = (int) totalQty;
        } else {
            // todo fix it
            System.out.println("Shares price is higher than amount could be allocated");
            return false;
            //totalQty = Double.parseDouble(df1.format( amountToPut/price));
        }
        if (totalQty < 1) {
            System.out.println("Shares price is higher than amount could be allocated");
            return false;
        }
        double stopPrice = price - price / 100 * stopPercent;
        Order order = new Order();
        order.action("BUY");
        order.orderType(OrderType.MKT);
        order.totalQuantity(Decimal.get(totalQty));
        orderId = orderId + 1;
        wrapper.getClient().placeOrder(orderId, contract, order);
        utils.pause(1000);
        if (wrapper.getErrorCode() > 0) {
            System.out.println("PlaceOrderAction: error code: " + wrapper.getErrorCode());
            return false;
        }

        utils.pause(2000);
        orderId = orderId + 1;
        order = new Order();
        order.action("SELL");
        order.orderType(OrderType.STP);
        order.totalQuantity(Decimal.get(totalQty));
        stopPrice = Double.parseDouble(df.format(stopPrice));
        order.auxPrice(stopPrice);
        wrapper.getClient().placeOrder(orderId + 1, contract, order);
        utils.pause(1000);
        return true;
    }


    private Integer getOrderId() {
        Integer orderId = wrapper.getCurrentOrderId();
        orderId++;
        return orderId;
    }

    private void placeOrder() {


    }
}
