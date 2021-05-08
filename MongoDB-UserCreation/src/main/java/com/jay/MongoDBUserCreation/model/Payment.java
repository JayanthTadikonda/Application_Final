package com.jay.MongoDBUserCreation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private int paymentId;
    private String customerName;
    private String washerName;
    private String paymentStatus;
    private String transactionId;
    private int orderId;
    private double amount;
    private LocalDateTime paymentDate;
    private String review;
    private int rating;
}
