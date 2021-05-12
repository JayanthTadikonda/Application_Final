package com.jay.CWOrderService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jay.CWOrderService.common.Payment;
import com.jay.CWOrderService.common.TransactionRequest;
import com.jay.CWOrderService.common.TransactionResponse;
import com.jay.CWOrderService.model.Order;
import com.jay.CWOrderService.repository.OrderRepository;
import com.jay.CWOrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/place-order")
    public Order placeOrder(@RequestBody Order order) throws JsonProcessingException {
        return orderService.payAfterWash(order);
    }

    @PostMapping("/book-wash")
    public TransactionResponse placeOrder(@RequestBody TransactionRequest request) {
        return orderService.saveOrder(request);
    }

    @GetMapping("/get-orders/{name}")
    public List<Order> getOrdersByName(@PathVariable String name) {
        return orderService.getOrderListByName(name);
    }

    @GetMapping("/washer-orders/{name}")
    public List<Order> getOrdersByWasherName(@PathVariable String name) {
        return orderService.getWasherOrderListByName(name);
    }

    @PostMapping("/update-status")
    public Order updatePaymentStatus(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @RequestMapping("/test-order")
    public String testOrder(){
        return "Order service running";
    }
}
