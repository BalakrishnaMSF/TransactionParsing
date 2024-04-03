package com.example.TransactionParsing.config;

import com.example.TransactionParsing.entity.Transaction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
// Other imports...

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
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Transaction transaction : transactions) {
                Row row = sheet.createRow(rowNum++);
                LocalDate localDate = transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                row.createCell(0).setCellValue(dateFormatter.format(localDate));
                row.createCell(1).setCellValue(transaction.getNotes());
                row.createCell(2).setCellValue(transaction.getAmount());
                row.createCell(3).setCellValue(transaction.getTransactionType().toString());
            }

            // Write to file with absolute path
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
