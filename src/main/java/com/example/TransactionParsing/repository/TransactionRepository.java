package com.example.TransactionParsing.repository;

import com.example.TransactionParsing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByDateBetween(Date fromDate, Date toDate);

}
