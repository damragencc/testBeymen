package com.beymen.tests;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelCreator {
    public static void main(String[] args) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("SearchTerms");
            
            // İlk satır: short
            Row row0 = sheet.createRow(0);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("short");
            
            // İkinci satır: gömlek
            Row row1 = sheet.createRow(1);
            Cell cell1 = row1.createCell(0);
            cell1.setCellValue("gömlek");
            
            // Excel dosyasını kaydet
            try (FileOutputStream fileOut = new FileOutputStream("src/test/resources/searchData.xlsx")) {
                workbook.write(fileOut);
                System.out.println("Excel dosyası başarıyla oluşturuldu!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
