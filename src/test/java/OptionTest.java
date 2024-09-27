import base.TestSetUp;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.trading.support.Calculator;
import com.trading.support.EMACalculator;
//import com.trading.support.SMACalculator;
import com.trading.support.VolumeCalculator;
import org.junit.Test;

public class OptionTest extends TestSetUp {


    //@Test
    public void testcurrency() throws Exception{
        Contract eurusdContract = new Contract();
        eurusdContract.symbol("EUR");
        eurusdContract.secType("CASH");
        eurusdContract.currency("USD");
        eurusdContract.exchange("IDEALPRO");
        m_client.reqMktDepth(1001, eurusdContract, 5, false, null);


        Thread.sleep(3000);
        System.out.println("TESTTT#######@@@@@");


    }
    //@Test
    public void testSmaMrkt() throws Exception{
        Contract contract = new Contract();
        m_client.reqHistoricalData(1000 + 10, new USStockContract("SMCI"), "", "200 D", "1 day", "MIDPOINT", 1, 1, false, null);
        Thread.sleep(3000);
       // Calculator calculator = new VolumeCalculator();
        //calculator.calculate(wrapper.getList());
        EMACalculator smaCalculator = new EMACalculator();
        smaCalculator.calculate(wrapper.getList(), 200);
       // System.out.printf("TESTTT#######@@@@@%s%n", );
    }

    //@Test
    public void testEMA() throws Exception{
        Contract contract = new Contract();
        m_client.reqHistoricalData(1000 + 10, new USStockContract("SMCI"), "", "200 D", "1 day", "MIDPOINT", 1, 1, false, null);
        Thread.sleep(3000);
        EMACalculator smaCalculator = new EMACalculator();
        smaCalculator.calculateEMA(wrapper.getList(), 13);
    }

    //@Test
    public void testVolumeMrkt() throws Exception{
        Contract contract = new Contract();


        m_client.reqHistoricalData(1000 + 10, new USStockContract("AMZN"), "", "200 D", "1 day", "TRADES", 1, 1, false, null);
        Thread.sleep(3000);
        Calculator calculator = new VolumeCalculator();
        calculator.calculate(wrapper.getList());
      //  SMACalculator smaCalculator = new SMACalculator();
        //smaCalculator.calculate(wrapper.getList(), 200);
        // System.out.printf("TESTTT#######@@@@@%s%n", );
    }


    @Test
    public void testPlaceOrder() throws InterruptedException {

        double accountValue= 10000;
        int risk = 1;
        double amountToPut = accountValue/100 * risk;
        Contract contract = new USStockContract("AMZN");
        Order order = new Order();
        int orderInitId=9898232;
        wrapper.nextValidId(orderInitId);
        int orderId = wrapper.getCurrentOrderId();
        orderId++;
        order.action("SELL");
        order.orderType("MKT");
        order.totalQuantity(Decimal.get(1.0));
        m_client.placeOrder(orderId, contract, order);
        Thread.sleep(3000);
        wrapper.nextValidId(orderId);



        /*
        double accountValue = 10000;
        int risk = 1;
        double amountToPut = accountValue / 100 * risk;

// Create contract for Amazon stock
        Contract contract = new USStockContract("AMZN");

// Create an order
        Order order = new Order();
        order.action("SELL");
        order.orderType("LMT");
        order.totalQuantity(Decimal.get(1.0));

// Get the next valid order ID
        wrapper.nextValidId(654500); // This method will asynchronously return the next valid ID
        int orderId = wrapper.getCurrentOrderId(); // Get the order ID provided by nextValidId()
        orderId++; // Increment it to ensure uniqueness
        orderId++;

// Use the dynamic order ID for placing the order
        m_client.placeOrder(orderId, contract, order); // Use the unique order ID

        Thread.sleep(3000); // Optional: Delay to simulate waiting for order submission or response
        wrapper.nextValidId(orderId);

         */

    }




}

class USStockContract extends Contract {
    public USStockContract(String symbol)
    {
        super.symbol(symbol);
        super.secType("STK");
        super.exchange("SMART");
        super.primaryExch("ISLAND");
        super.currency("USD");
    }
}

