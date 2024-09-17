package com.forex;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import com.forex.excel.SheetHandler;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    private static volatile boolean keepRunning = true;
    static EClientSocket m_client;
    static EWrapperImpl wrapper;

    public static void main(String[] args) {
        wrapper = new EWrapperImpl();

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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered. Disconnecting gracefully...");
            gracefulShutdown();
        }));

    }

    private static void gracefulShutdown() {
        Workbook workbook = new XSSFWorkbook();
        SheetHandler sheetHandler = new SheetHandler();
        org.apache.poi.ss.usermodel.Sheet sheet = sheetHandler.createSheet(workbook);

        keepRunning = false;
        if (m_client != null && m_client.isConnected()) {
            m_client.cancelMktDepth(1001, true);  // Cancel the market depth request (optional)
            m_client.eDisconnect();         // Ensure the client is disconnected
            System.out.println("Client disconnected, exiting gracefully.");
        }
        int rowNumber = 0;
        for (String[] rowData : wrapper.getDataArray()) {
            Row row = sheet.createRow(rowNumber++);  // Create a new row for each entry
            int colIndex = 0;
            for (String cellData : rowData) {
                Cell cell = row.createCell(colIndex++);
                cell.setCellValue(cellData);  // Set the value for each cell
            }
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < wrapper.getDataArray().get(0).length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("batch_output.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Excel file 'batch_output.xlsx' created successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        try {
            workbook.close();
        } catch (IOException e) {
             e.printStackTrace();
        }
    }
}



