package com.example.TransactionParsing.controller;


import com.example.TransactionParsing.entity.Transaction;
import com.example.TransactionParsing.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadStudentsData(@RequestParam("file") MultipartFile file){
        try {
            transactionService.saveTransaction(file);
//            List<Transaction> transactions = transactionService.getTransactions();
            return ResponseEntity.ok("Data saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok("Data not saved successfully");
        }
    }

    @GetMapping("/getTransaction")
    public ResponseEntity<?> getStudentsData(){
        List<Transaction> transactions = transactionService.getTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/getTransactionsBetweenDates")
    public ResponseEntity<?> getTransactionsBetweenDates(@RequestParam("startDate") String startDateStr,
                                                         @RequestParam("endDate") String endDateStr){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            List<Transaction> transactions = transactionService.getTransactionsBetweenDates(startDate, endDate);
            return ResponseEntity.ok(transactions);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
    }

}

