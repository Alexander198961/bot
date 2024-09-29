package com.trading;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.trading.gui.MainForm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    static EClientSocket m_client;
    static EWrapperImpl wrapper;

    public static void main(String[] args) {

        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        int port = Integer.parseInt(properties.getProperty("port"));
        String host =properties.getProperty("host");
        host="localhost";
        port=7496;
        System.out.println("ib tws port="+ port);
        System.out.println("ib tws host="+ host);
        assert port>0;
        assert !host.equals("");
        wrapper = new EWrapperImpl();
        IBSignalHandler ibSignalHandler = new IBSignalHandler(wrapper, port, host);
        ibSignalHandler.run();
        m_client = wrapper.getClient();
        if (m_client.isConnected()) {
            System.out.println("Connected to TWS");
        }else{
            System.out.println("Couldnot connect");
            System.exit(10);
        }


        MainForm mainForm = new MainForm(wrapper);
        mainForm.display();



    }

    private static void gracefulShutdown() {

    }
}



