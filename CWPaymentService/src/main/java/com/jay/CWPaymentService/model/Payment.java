package com.jay.CWPaymentService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collection = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    private ObjectId _id;
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

    @Override
    public String toString() {
        return "Hey " + customerName + "! \n " +
                " Payment Receipt for your Recent CAR WASH ! " + "\n " +
                " customerName  : " + customerName + " \n " +
                " washerName    : " + washerName + "\n " +
                " orderId       : " + orderId + "\n " +
                " paymentId     : " + paymentId + "\n " +
                " transactionId : " + transactionId + "\n " +
                " paymentStatus : " + paymentStatus + "\n " +
                " amount        : " + amount + "\n " +
                " paymentDate   : " + paymentDate + "\n " +
                " review        : " + review + "\n " +
                " rating        : " + rating + "\n ";
    }
}
