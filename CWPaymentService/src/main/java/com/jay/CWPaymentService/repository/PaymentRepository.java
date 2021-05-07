package com.jay.CWPaymentService.repository;

import com.jay.CWPaymentService.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, Integer> {

    @Override
    List<Payment> findAll();
}
