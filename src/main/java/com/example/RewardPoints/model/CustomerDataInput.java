package com.example.RewardPoints.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CustomerDataInput {
    private String customerId;
    private double transactionAmount;//In Dollars
    private LocalDate transactionDate;//Format : YYYY-MM-DD
}
