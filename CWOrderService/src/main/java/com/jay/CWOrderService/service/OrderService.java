package com.jay.CWOrderService.service;

import com.jay.CWOrderService.common.*;
import com.jay.CWOrderService.model.Order;
import com.jay.CWOrderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AutomatedOrderId orderId;

    public TransactionResponse saveOrder(TransactionRequest request) {

        String response = "";

        Customer customer = restTemplate.getForObject("http://customer-microservice/customer/get-customer/" + request.getOrder().getCustomerName(), Customer.class);

        Order order = request.getOrder();
        assert customer != null;
        order.setCarModel(customer.getCarModel());

        order.setPaymentStatus("Pending");
        orderRepository.save(order);
        Payment payment = new Payment();
        payment.setCustomerName(order.getCustomerName());
        payment.setWasherName(order.getWasherName());
        payment.setOrderId(order.getOrderId());
        payment.setAmount(order.getAmount());

        Payment paymentResponse = restTemplate.postForObject("http://payment-microservice/payment/pay-now", payment, Payment.class);

        orderRepository.save(order);
        assert paymentResponse != null;
        response = paymentResponse.getPaymentStatus().equalsIgnoreCase("success") ? "payment Successful, Order Booked" : "Sorry, payment failed !";
        return new TransactionResponse(order, paymentResponse.getTransactionId(), paymentResponse.getAmount(), response, null);
    }

    public Order payAfterWash(Order order) {

        Random random = new Random();
        order.setOrderId(random.nextInt(9999));
        order.setPaymentStatus("Pending");

        if (order.getWashName().contains("basic-wash")) {
            order.setAmount(999);

        } else {
            order.setAmount(555);
        }
        orderRepository.save(order);
        return order;
    }

    public List<Order> getOrderListByName(String name) {
        return orderRepository.findAll().stream().filter(a -> a.getCustomerName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }
}