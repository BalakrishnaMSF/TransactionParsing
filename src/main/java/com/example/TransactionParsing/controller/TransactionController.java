package com.example.TransactionParsing.controller;


import com.example.TransactionParsing.entity.Transaction;
import com.example.TransactionParsing.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}

