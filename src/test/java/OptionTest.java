import base.TestSetUp;

import com.ib.client.Contract;
import org.junit.Test;

public class OptionTest extends TestSetUp {


    @Test
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

}
