package com.jay.CWPaymentService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jay.CWPaymentService.model.Payment;
import com.jay.CWPaymentService.model.TransactionRequest;
import com.jay.CWPaymentService.model.TransactionResponse;
import com.jay.CWPaymentService.repository.PaymentRepository;
import com.jay.CWPaymentService.service.PaymentService;
import it.ozimov.springboot.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    public EmailService emailService;

    @PostMapping("/pay")
    public TransactionResponse payAmount(@RequestBody TransactionRequest request) throws AddressException, JsonProcessingException {
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

    @GetMapping("/send-mail")
    public String sendMail() throws AddressException {
        Payment payment = new Payment();
        payment.setPaymentId(1232);
        payment.setPaymentStatus("Success");
        paymentService.sendEmail(payment,"jayanth2683@gmail.com");
        return "Sent Mail";
    }

    @GetMapping("/send-dummy")
    public String sendDummy() throws AddressException {
        paymentService.sendEmailDummy();
        return "SEnt MAIL";
    }

    @RequestMapping("/test-payment")
    public String testPayment(){
        return "Payment gateway up and running";
    }
}
