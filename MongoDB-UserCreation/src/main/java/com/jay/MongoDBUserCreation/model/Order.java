package com.jay.MongoDBUserCreation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int orderId;
    private String washName;
    private String carModel;
    private double amount;
    private String customerName;
    private String washerName;
    private Date date;
    private String paymentStatus;

}
