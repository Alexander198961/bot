import base.TestSetUp;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.trading.scan.CrossScan;
import com.trading.scan.PlaceOrderAction;
import com.trading.scan.SaveTickerAction;
import com.trading.scan.Scan;
import com.trading.support.Calculator;
import com.trading.support.EMACalculator;
//import com.trading.support.SMACalculator;
import com.trading.support.VolumeCalculator;
import com.trading.support.reader.TickerReader;
import com.trading.tickers.FtpDownloader;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class OptionTest extends TestSetUp {


   // @Test
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

    @Test
    public void testDownload(){

    }
   // @Test
    public void testReadFile() throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("nasdaqlisted.txt")).toURI());

        Stream<String> lines = Files.lines(path);
        Stream<String> limited =  lines.limit(10);
        List<String> tickers = new ArrayList<>();
        limited.forEach(line ->{

            tickers.add(line.split("\\|")[0]);
        });
        tickers.remove(0);
        System.out.println(tickers);

    }
    @Test
    public void testFast(){
        FtpDownloader ftpDownloader = new FtpDownloader();
        ftpDownloader.downloadToText();
        //System.out.println("fast===" + );
    }
    //@Test
    public void testSmaMrkt() throws Exception{
        Contract contract = new Contract();
        m_client.reqHistoricalData(1000 + 10, new USStockContract("AMZN"), "", "200 D", "1 day", "MIDPOINT", 1, 1, false, null);
        Thread.sleep(3000);
       // Calculator calculator = new VolumeCalculator();
        //calculator.calculate(wrapper.getList());
        List<Bar> list = wrapper.getList();
        EMACalculator smaCalculator = new EMACalculator();
        double smaLastValue= smaCalculator.calculate(wrapper.getList(), 200);
        double lastClosePrice = list.get(list.size() -1).close();
        if(lastClosePrice> smaLastValue ){
               System.out.println("sma value====");
        }
       // System.out.printf("TESTTT#######@@@@@%s%n", );
    }

   // @Test
    public void testEMA() throws Exception{
        Contract contract = new Contract();
        m_client.reqHistoricalData(1000 + 10, new USStockContract("SMCI"), "", "200 D", "1 day", "MIDPOINT", 1, 1, false, null);
        Thread.sleep(3000);
        EMACalculator smaCalculator = new EMACalculator();
        smaCalculator.calculateEMA(wrapper.getList(), 13);
    }

   // @Test
    public void sp500ticker() throws IOException {
        //SP500Scraper sp500Scraper = new SP500Scraper();
        //System.out.println("fetch==="+ sp500Scraper.fetch());
    }
   // @Test
    public void scannerCross() throws InterruptedException {
        //acmr axdx
        //TickerReader tickerReader = new TickerReader();
        //List<String> tickers = tickerReader.tickers();
       // Scan scan = new CrossScan(9,26);
       // scan.scan(wrapper, new PlaceOrderAction(wrapper, 10000, 1), tickers);
    }

   // @Test
    public void testVolumeMrkt() throws Exception{
        /*
        TickerReader tickerReader = new TickerReader();
        List<String> tickers = tickerReader.tickers();
        final double percent = 1.2;
        List <String> tickersMetCriteria = new ArrayList<>();
        for(String ticker : tickers) {
            m_client.reqHistoricalData(1000 + 10, new USStockContract(ticker), "", "200 D", "1 day", "TRADES", 1, 1, false, null);
            Thread.sleep(1000);
            Calculator calculator = new VolumeCalculator();
            double averageVolume = calculator.calculate(wrapper.getList());
            List<Bar> list = wrapper.getList();

            double lastVolume = list.get(list.size() - 1).volume().longValue();
            if (lastVolume > percent * averageVolume) {
                tickersMetCriteria.add(ticker);
               // System.out.println("Last volume is ===============" + lastVolume);
            }

        }
        System.out.println(tickersMetCriteria);
        //calculator.calculate(wrapper.getList());
      //  SMACalculator smaCalculator = new SMACalculator();
        //smaCalculator.calculate(wrapper.getList(), 200);
        // System.out.printf("TESTTT#######@@@@@%s%n", );

         */
    }


    //@Test
    public void testPlaceOrder() throws InterruptedException {

        double accountValue= 10000;
        int risk = 1;
        double amountToPut = accountValue/100 * risk;
        double price =191.25;
        double stopPrice= price - price/100* 4;
        Contract contract = new USStockContract("AMZN");
        Order order = new Order();
        int orderInitId=9999239;
        wrapper.nextValidId(orderInitId);
        int orderId = wrapper.getCurrentOrderId();
        orderId++;
        order.action("BUY");
        order.orderType("MKT");
        order.totalQuantity(Decimal.get(1.0));
        m_client.placeOrder(orderId, contract, order);
        Thread.sleep(2000);
        wrapper.nextValidId(orderId);
        orderId=wrapper.getCurrentOrderId();
        orderId++;
        order.action("SELL");
        order.orderType("LMT");
        order.totalQuantity(Decimal.get(1.0));
        order.lmtPrice(stopPrice);
        m_client.placeOrder(orderId, contract, order);
        Thread.sleep(2000);


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

