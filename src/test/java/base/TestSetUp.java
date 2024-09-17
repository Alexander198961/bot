package base;
import com.ib.client.EClientSocket;

import com.forex.EWrapperImpl;
import com.forex.IBSignalHandler;


import org.junit.Before;

public class TestSetUp {

    protected EWrapperImpl wrapper = new EWrapperImpl();

    protected EClientSocket m_client = wrapper.getClient();
    protected IBSignalHandler ibSignalHandler = new IBSignalHandler(wrapper);

    @Before
    public void setUp() {
        ibSignalHandler.run();
    }




}
