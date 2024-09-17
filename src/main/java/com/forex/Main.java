package com.forex;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    private static volatile boolean keepRunning = true;
    static EClientSocket m_client ;

    public static void main(String [] args){
        EWrapperImpl wrapper = new EWrapperImpl();

       IBSignalHandler ibSignalHandler = new IBSignalHandler(wrapper);
        ibSignalHandler.run();
        m_client = wrapper.getClient();
        //m_client.eConnect("127.0.0.1", 7496, 0);  // Client ID = 0
        if (m_client.isConnected()) {
            System.out.println("Connected to TWS");
        }

        Contract eurusdContract = new Contract();
        eurusdContract.symbol("EUR");
        eurusdContract.secType("CASH");
        eurusdContract.currency("USD");
        eurusdContract.exchange("IDEALPRO");
        m_client.reqMktDepth(1001, eurusdContract, 5, false, null);

        try {
            // Keep the main thread running to allow the processing thread to work
            while (keepRunning) {
                Thread.sleep(1000);  // Simple keep-alive loop
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            Thread.currentThread().interrupt();  // Restore interrupt status
        }
        /*
        try {
            Thread.sleep(3000);
           // System.exit(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

         */

        stop();
    }
    public static void stop() {
        keepRunning = false;  // Stop any running loops (if applicable)
        if (m_client != null && m_client.isConnected()) {
            m_client.eDisconnect();  // Ensure the client is disconnected
            System.out.println("Client disconnected, exiting.");
        }
    }
}
