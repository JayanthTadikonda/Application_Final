package com.jay.CWOrderService.service;

import com.jay.CWOrderService.common.Payment;
import com.jay.CWOrderService.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class AutomatedOrderId {

    @Autowired
    private MongoOperations mongo;

    public int getNextOrderId(String entityName){

        Order counter = mongo.findAndModify(
                query(where("orderId").is(entityName)),
                new Update().inc("orderId",(3*7+3-2*2/3+2)),
                options().returnNew(true).upsert(true),
                Order.class);
        assert counter != null;
        return counter.getOrderId();
    }
}
