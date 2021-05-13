package com.jay.CWPaymentService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jay.CWPaymentService.model.Payment;
import com.jay.CWPaymentService.model.TransactionRequest;
import com.jay.CWPaymentService.model.TransactionResponse;

import javax.mail.internet.AddressException;
import java.util.List;
import java.util.Optional;

public interface PaymentService {

    public void sendEmailDummy() throws AddressException;

    public void sendEmail(Payment payment, String emailAddress) throws AddressException;

    public TransactionResponse doPaymentSetOrderPaymentStatus(TransactionRequest request) throws AddressException, JsonProcessingException;

    public String paymentProcessing();

    public List<Payment> paymentList(String name);

    public Optional<Payment> paymentById(int id) throws JsonProcessingException;

}
