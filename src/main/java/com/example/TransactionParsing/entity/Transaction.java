package com.example.TransactionParsing.entity;

import com.example.TransactionParsing.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("Date")
    private Date date;

    @JsonProperty("Notes")
    private String notes;

    @JsonProperty("Amount")
    private double amount;

    @JsonProperty("Type")
    private TransactionType transactionType;
}
