package com.jay.CWWasherMicroservice.repository;

import com.jay.CWWasherMicroservice.model.Washer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WasherRepository extends MongoRepository<Washer, Integer> {

    public List<Washer> findAll();

    public Washer findByName(String name);
}
