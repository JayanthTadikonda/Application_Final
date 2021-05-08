package com.jay.CWOrderService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private ObjectId _id;
    private int orderId;
    private String washName;
    private String carModel;
    private double amount;
    private String customerName;
    private String washerName;
    private Date date;
    private String paymentStatus;
    private String emailAddress;

}
