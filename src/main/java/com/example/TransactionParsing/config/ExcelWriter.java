package com.example.TransactionParsing.config;

import com.example.TransactionParsing.entity.Transaction;
import com.example.TransactionParsing.enums.TransactionType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFTableColumn;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.table.TableColumn;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ExcelWriter {

    public static void writeTransactionsToExcel(List<Transaction> transactions, String filePath, Date startDate, Date endDate) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transactions");

            // Calculate total credit, total debit, and net amount
            double totalCredit = 0.0;
            double totalDebit = 0.0;
            for (Transaction transaction : transactions) {
                if (transaction.getTransactionType() == TransactionType.CREDIT) {
                    totalCredit += transaction.getAmount();
                } else if (transaction.getTransactionType() == TransactionType.DEBIT) {
                    totalDebit += transaction.getAmount();
                }
            }
            double netAmount = totalCredit - totalDebit;

            Row details_row = sheet.createRow(0);
            details_row.createCell(1).setCellValue("ICICI Bank-Perungudi");
            Row details_row1 = sheet.createRow(1);
            details_row1.createCell(1).setCellValue("Chennai, Tamilnadu");

            // Display details at the top of the sheet
            Row detailsRow = sheet.createRow(3);
            detailsRow.createCell(0).setCellValue("Total Credit: " + totalCredit);
            Row details = sheet.createRow(4);
            details.createCell(0).setCellValue("Total Debit: " + totalDebit);
            Row detail = sheet.createRow(5);
            detail.createCell(0).setCellValue("Net Amount: " + netAmount);
            Row detail2 = sheet.createRow(6);
            detail2.createCell(0).setCellValue("Transactions between the time period");

            // Create header row
            Row headerRow = sheet.createRow(8);
            headerRow.createCell(0).setCellValue("Date");
            headerRow.createCell(1).setCellValue("Notes");
            headerRow.createCell(2).setCellValue("Amount");
            headerRow.createCell(3).setCellValue("Transaction Type");

            // Write data rows
            int rowNum = 9; // Start after details and header rows
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Transaction transaction : transactions) {
                Row row = sheet.createRow(rowNum++);
                LocalDate localDate = transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                row.createCell(0).setCellValue(dateFormatter.format(localDate));
                row.createCell(1).setCellValue(transaction.getNotes());
                row.createCell(2).setCellValue(transaction.getAmount());
                row.createCell(3).setCellValue(transaction.getTransactionType().toString());
            }

            // Define the data range for the table
            AreaReference dataRange = new AreaReference(
                    new CellReference(8, 0), // Start cell
                    new CellReference(rowNum - 1, 3), // End cell
                    workbook.getSpreadsheetVersion()
            );

            // Create the table
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFTable table = xssfSheet.createTable(dataRange);

            // Set the table name
            table.getCTTable().setName("TransactionTable");

            // Style the table
            table.getCTTable().addNewTableStyleInfo().setName("TableStyleMedium9");

            // Create table columns
            XSSFTableColumn column;
            for (int i = 0; i < 4; i++) {
                column = table.getColumns().get(i);
                column.setName(headerRow.getCell(i).getStringCellValue());
            }

            // Auto filter for the table
            table.getCTTable().addNewAutoFilter().setRef(dataRange.formatAsString());

            // Write to file with absolute path
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
