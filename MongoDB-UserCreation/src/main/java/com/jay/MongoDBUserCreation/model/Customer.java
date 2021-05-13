package com.jay.MongoDBUserCreation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {


    private int id;
    private String name;
    private String password;
    private List<String> address;
    private String carModel;
    private String emailAddress;
    private String role;

}
