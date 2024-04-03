package com.example.TransactionParsing.service.Impl;

import com.example.TransactionParsing.config.ExcelToDatabase;
import com.example.TransactionParsing.entity.Transaction;
import com.example.TransactionParsing.repository.TransactionRepository;
import com.example.TransactionParsing.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(MultipartFile multipartFile) {
        try {
            List<Transaction> students = ExcelToDatabase.getTransactions(multipartFile.getInputStream());
            transactionRepository.saveAll(students);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsBetweenDates(Date startDate, Date endDate) {
        return transactionRepository.findByDateBetween(startDate, endDate);
    }
}
