package com.trading.scan;

import com.ib.client.*;
import com.trading.EWrapperImpl;
import com.trading.api.USStockContract;
import com.trading.cache.Cache;
import com.trading.data.TradeHistory;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class PlaceOrderAction extends Action {
    private double accountValue = 10000;
    private double risk = 1;
    private final double stopPercent;
    private final double trailingStopPrice;

    public TradeHistory getTradeHistory() {
        return tradeHistory;
    }

    private TradeHistory tradeHistory = new TradeHistory();

    private JTextArea textArea;

    public PlaceOrderAction(EWrapperImpl eWrapperImpl, JTextArea textArea , double accountValue, double risk, double stopPercent, double trailingStopPrice) {
        this.wrapper = eWrapperImpl;
        this.textArea = textArea;
        this.accountValue = accountValue;
        this.risk = risk;
        this.stopPercent = stopPercent;
        this.trailingStopPrice = trailingStopPrice;

    }

    ;

    private final EWrapperImpl wrapper;

    @Override
    public boolean execute(List<Bar> list, String ticker) {

        Utils utils = new Utils();
        int orderId = 0;
        double accountValue = this.accountValue;
        double risk = this.risk;
        wrapper.getClient().reqIds(1);
        wrapper.setLastPrice(0);
        orderId = getOrderId();
        Contract contract = new USStockContract(ticker);
        wrapper.getClient().reqMktData(orderId, contract, "", false, false, null);
        utils.pause(1000);
        double amountToPut = accountValue / 100 * risk;
        DecimalFormat df = new DecimalFormat("#.##");
        //double price =list.get(list.size() -1).close();
        double price = wrapper.getLastPrice();
        if (price == 0 || price<0) {
            // todo: return it
           // return false;
            price = list.get(list.size() -1).close();
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
        String text = textArea.getText();
        text = text + "\n" + "LONG " + ticker + " Quantity " + String.valueOf(totalQty) +  "  Price " +  price;
        textArea.setText(text);
       // tradeHistory.setQuantity(String.valueOf(totalQty));
        //tradeHistory.setPrice(df.format(price));
        //tradeHistory.setTicker(ticker);
        // fix trailing
       // Map<String, Entry> mapTickersState = (Map) Cache.cache.getIfPresent(Cache.Keys.Trailing + Cache.Keys.tickersStateMap.name());


       // if (mapTickersState != null && !mapTickersState.isEmpty() && mapTickersState.get(ticker) != null && mapTickersState.get(ticker).getEnabled() == true) {
            orderId = orderId + 1;
            order = new Order();
            order.action("SELL");
            order.orderType(OrderType.STP);
            order.totalQuantity(Decimal.get(totalQty));
            stopPrice = Double.parseDouble(df.format(stopPrice));
            order.auxPrice(stopPrice);
            wrapper.getClient().placeOrder(orderId + 1, contract, order);
            utils.pause(1000);
            Integer rowNumber = (Integer) Cache.cache.getIfPresent(ticker+ Cache.Keys.RowNumber);
            if (rowNumber != null) {
                Entry<Boolean> entryIsEnabled = (Entry<Boolean>) Cache.cache.getIfPresent(Cache.Keys.Trailing.name() + rowNumber);
                if(entryIsEnabled.getEntry().booleanValue()) {
                    orderId = orderId + 1;
                    order = new Order();
                    order.action("SELL");
                    order.orderType(OrderType.TRAIL);
                    order.totalQuantity(Decimal.get(totalQty));
                    order.trailingPercent(trailingStopPrice);
                    wrapper.getClient().placeOrder(orderId + 1, contract, order);
                    utils.pause(2000);
                }
            }
      //  }
        //order.trailStopPrice(trailingStopPrice);
        //order.adjustedTrailingAmount();
       // stopPrice = Double.parseDouble(df.format(stopPrice));
       // order.auxPrice(stopPrice);




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
