package base;

import org.junit.Test;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.*;

import java.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelTest {

    @Test
    public void test() throws IOException {
        // Create a workbook (XSSFWorkbook for .xlsx format)
        Workbook workbook = new XSSFWorkbook();

        // Create a sheet
        Sheet sheet = workbook.createSheet("Data");

        // Create a list of data to write (batch data)
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Name", "Age"});
        data.add(new String[]{"1", "John Doe", "30"});
        data.add(new String[]{"2", "Jane Smith", "25"});
        data.add(new String[]{"3", "Mike Johnson", "45"});
        data.add(new String[]{"4", "Emily Davis", "35"});

        // Write the batch data to the sheet
        int rowIndex = 0;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowIndex++);  // Create a new row for each entry
            int colIndex = 0;
            for (String cellData : rowData) {
                Cell cell = row.createCell(colIndex++);
                cell.setCellValue(cellData);  // Set the value for each cell
            }
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < data.get(0).length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("batch_output.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Excel file 'batch_output.xlsx' created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

