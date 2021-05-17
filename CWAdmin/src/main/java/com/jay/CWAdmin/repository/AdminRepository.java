package com.jay.CWAdmin.repository;

import com.jay.CWAdmin.model.WashPack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends MongoRepository<WashPack, Integer> {


}
