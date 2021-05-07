package com.jay.SpringSecurityJWT.repository;

import com.jay.SpringSecurityJWT.model.Customer;
import com.jay.SpringSecurityJWT.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<Customer, Integer>{

    public Customer findByUsername(String name);

}
