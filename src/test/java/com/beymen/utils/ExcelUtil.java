package com.beymen.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {
    public static String[] readSearchTerms(String filePath) throws IOException {
        String[] searchTerms = new String[2];
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            searchTerms[0] = sheet.getRow(0).getCell(0).getStringCellValue(); // short
            searchTerms[1] = sheet.getRow(0).getCell(1).getStringCellValue(); // gömlek
        }
        return searchTerms;
    }
}
