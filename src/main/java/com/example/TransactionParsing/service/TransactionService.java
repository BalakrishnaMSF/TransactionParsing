package com.example.TransactionParsing.service;

import com.example.TransactionParsing.entity.Transaction;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionService {
    void saveTransaction(MultipartFile multipartFile);
    List<Transaction> getTransactions();
}
