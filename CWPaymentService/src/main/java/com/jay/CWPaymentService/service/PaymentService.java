package com.jay.CWPaymentService.service;

import com.jay.CWPaymentService.model.Order;
import com.jay.CWPaymentService.model.Payment;
import com.jay.CWPaymentService.model.TransactionRequest;
import com.jay.CWPaymentService.model.TransactionResponse;
import com.jay.CWPaymentService.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AutomatedPaymentId paymentId;

    @Autowired
    private RestTemplate restTemplate;

    Random random = new Random();


    public TransactionResponse doPaymentSetOrderPaymentStatus(TransactionRequest request) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy/HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        dtf.format(currentTime);
        Payment payment = request.getPayment();
        Order order = request.getOrder();
        payment.setPaymentId(random.nextInt(9999));
        payment.setPaymentStatus(paymentProcessing());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentDate(currentTime);
        if (payment.getPaymentStatus().equalsIgnoreCase("success")) {
            order.setPaymentStatus("Paid");
            restTemplate.postForObject("http://order-microservice/order/update-status", order, Order.class);
            paymentRepository.save(payment);
            return new TransactionResponse(order, payment.getTransactionId(), payment.getAmount(), "Payment Successful");
        } else {
            return new TransactionResponse(order, payment.getTransactionId(), payment.getAmount(), "Payment Failed Please try again!");
        }
    }

    public String paymentProcessing() {
        //3rd party api payment gateway (BrainTree)
        return new Random().nextBoolean() ? "success" : "payment failed, please try again !";
    }

    public List<Payment> paymentList(String name) {
        return paymentRepository.findAll()
                .stream().filter(payment -> payment.getCustomerName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
}
