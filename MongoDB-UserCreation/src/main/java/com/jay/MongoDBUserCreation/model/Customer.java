package com.jay.MongoDBUserCreation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

//    @Id
//    private ObjectId _id;
    private int id;
    @NotEmpty(message = "Name cannot be null, Please enter your name !")
    private String name;
    @Length(min = 4,message = "Password must be of Minimum 4 characters")
    private String password;
    @NotEmpty(message = "Please provide the address for the washer partner to reach for.")
    private List<String> address;
    @NotEmpty(message = "Tell us your Car Model (SUV/RV/Saloon/Pick-Up/hatch-back/sport)")
    private String carModel;
    @Email(message = "Provide a valid E-mail Address ! ")
    private String emailAddress;
    private String role;

}
