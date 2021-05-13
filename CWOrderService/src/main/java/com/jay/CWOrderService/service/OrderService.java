package com.jay.CWOrderService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jay.CWOrderService.common.TransactionRequest;
import com.jay.CWOrderService.common.TransactionResponse;
import com.jay.CWOrderService.model.Order;

import java.util.List;

public interface OrderService {

    public TransactionResponse saveOrder(TransactionRequest request);

    public Order payAfterWash(Order order) throws JsonProcessingException;

    public List<Order> getOrderListByName(String name);

    public List<Order> getWasherOrderListByName(String name);

}
