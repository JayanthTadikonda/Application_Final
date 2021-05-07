package com.jay.CWPaymentService.controller;

import com.jay.CWPaymentService.model.Order;
import com.jay.CWPaymentService.model.Payment;
import com.jay.CWPaymentService.model.TransactionRequest;
import com.jay.CWPaymentService.model.TransactionResponse;
import com.jay.CWPaymentService.repository.PaymentRepository;
import com.jay.CWPaymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public TransactionResponse payAmount(@RequestBody TransactionRequest request) {
        return paymentService.doPaymentSetOrderPaymentStatus(request);
    }

    @GetMapping("/get-payments-list")
    public List<Payment> paymentList() {
        return paymentRepository.findAll();
    }

    @GetMapping("/get-payment/{name}")
    public List<Payment> paymentListOfName(@PathVariable String name) {
        return paymentService.paymentList(name);
    }
}
