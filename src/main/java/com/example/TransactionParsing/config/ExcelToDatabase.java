package com.example.TransactionParsing.config;

import com.example.TransactionParsing.entity.Transaction;
import com.example.TransactionParsing.enums.TransactionType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelToDatabase {

    private static final Logger logger = Logger.getLogger(ExcelToDatabase.class.getName());

    public static List<Transaction> getTransactions(InputStream inputStream) {

        if (inputStream != null) {
            List<Transaction> transactions = new ArrayList<>();

            try {
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

                int lastRowNum = sheet.getLastRowNum(); // Get the index of the last row containing data

                for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) { // Start from index 1 to skip header row
                    Row row = sheet.getRow(rowIndex);
                    if (row == null || isRowEmpty(row)) {
                        break; // Stop iterating if an empty row is encountered
                    }
                    int rowNum = row.getRowNum(); // Get the current row number
                    Iterator<Cell> cellIterator = row.iterator();
                    int cellIndex = 0;
                    Transaction transaction = new Transaction();
                    boolean validRow = true;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    transaction.setDate(cell.getDateCellValue());
                                } else {
                                    validRow = false;
                                    logger.log(Level.INFO, "Skipping row {0} due to invalid date format.", rowNum);
                                }
                                break;
                            case 1:
                                if (cell.getCellType() == CellType.STRING) {
                                    transaction.setNotes(cell.getStringCellValue());
                                } else {
                                    validRow = false;
                                    logger.log(Level.INFO, "Skipping row {0} due to non-string notes.", rowNum);
                                }
                                break;
                            case 2:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    transaction.setAmount(cell.getNumericCellValue());
                                } else {
                                    validRow = false;
                                    logger.log(Level.INFO, "Skipping row {0} due to non-numeric amount.", rowNum);
                                }
                                break;
                            case 3:
                                if (cell.getCellType() == CellType.STRING) {
                                    transaction.setTransactionType(TransactionType.valueOf(cell.getStringCellValue()));
                                } else {
                                    validRow = false;
                                    logger.log(Level.INFO, "Skipping row {0} due to invalid transaction type.", rowNum);
                                }
                                break;
                        }
                        cellIndex++;
                    }
                    if (validRow) {
                        transactions.add(transaction);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return transactions;
        } else {
            return null;
        }
    }

    private static boolean isRowEmpty(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
