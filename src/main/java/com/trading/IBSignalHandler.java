package com.trading;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import java.io.IOException;

public class IBSignalHandler implements Runnable{
    int port ;
    String host;

    public IBSignalHandler(EWrapperImpl wrapper, int port, String host) {
        this.wrapper = wrapper;
        this.port = port;
        this.host = host;
    }
    EClientSocket m_client;
    private EWrapperImpl wrapper;

    @Override
    public void run() {

        m_client = wrapper.getClient();
        final EReaderSignal m_signal = wrapper.getSignal();
        m_client.eConnect(host, port, 0);
        assert m_client.isConnected();
        final EReader reader = new EReader(m_client, m_signal);
        reader.start();
        new Thread(() -> {
            while (m_client.isConnected()) {
                m_signal.waitForSignal();
                System.out.println("message processing");
                try {
                    reader.processMsgs();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IllegalStateException(e.getMessage());
                }
                //System.exit(0);

            }
        }).start();
        //m_client.reqMarketDataType(4);
    }
}
