package com.jay.MongoDBUserCreation.repository;

import com.jay.MongoDBUserCreation.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, Integer> {

    public List<Customer> findAll();
    public Customer findByName(String name);
    public Customer findById(int id);

}
