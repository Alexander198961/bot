package com.forex.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SheetHandler {
    public Sheet createSheet(){
        Workbook workbook = new XSSFWorkbook();
        return workbook.createSheet("Data");
    }
}
