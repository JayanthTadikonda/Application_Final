package com.jay.CWPaymentService.service;

import com.jay.CWPaymentService.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class AutomatedPaymentId {

    @Autowired
    private MongoOperations mongo;

    public int getNextPaymentId(String entityName) {

        Payment counter = mongo.findAndModify(
                query(where("_id").is(entityName)),
                new Update().inc("paymentId", 1),
                options().returnNew(true).upsert(true),
                Payment.class);
        assert counter != null;
        return counter.getPaymentId();
    }
}
