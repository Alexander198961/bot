package base;
import com.ib.client.EClientSocket;

import com.trading.EWrapperImpl;
import com.trading.IBSignalHandler;


import org.junit.Before;

public class TestSetUp {

    protected EWrapperImpl wrapper = new EWrapperImpl();

    protected EClientSocket m_client = wrapper.getClient();
    protected IBSignalHandler ibSignalHandler = new IBSignalHandler(wrapper, 7496, "127.0.0.1");

    @Before
    public void setUp() {
        ibSignalHandler.run();
    }




}
