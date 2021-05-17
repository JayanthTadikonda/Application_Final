package com.jay.CWAdmin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
