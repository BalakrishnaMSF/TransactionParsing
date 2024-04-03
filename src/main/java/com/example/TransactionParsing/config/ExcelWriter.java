package com.example.TransactionParsing.config;

import com.example.TransactionParsing.entity.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    public static void writeTransactionsToExcel(List<Transaction> transactions, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transactions");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Date");
            headerRow.createCell(1).setCellValue("Notes");
            headerRow.createCell(2).setCellValue("Amount");
            headerRow.createCell(3).setCellValue("Transaction Type");

            // Write data rows
            int rowNum = 1;
            for (Transaction transaction : transactions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.getDate().toString());
                row.createCell(1).setCellValue(transaction.getNotes());
                row.createCell(2).setCellValue(transaction.getAmount());
                row.createCell(3).setCellValue(transaction.getTransactionType().toString());
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

