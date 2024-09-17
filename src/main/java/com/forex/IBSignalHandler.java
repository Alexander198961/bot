package com.forex;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;

import java.io.IOException;

public class IBSignalHandler implements Runnable{
    public IBSignalHandler(EWrapperImpl wrapper) {
        this.wrapper = wrapper;
    }
    EClientSocket m_client;
    private EWrapperImpl wrapper;

    @Override
    public void run() {

        m_client = wrapper.getClient();
        final EReaderSignal m_signal = wrapper.getSignal();
        m_client.eConnect("127.0.0.1", 7496, 0);
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

            }
        }).start();
        m_client.reqMarketDataType(4);
    }
}
