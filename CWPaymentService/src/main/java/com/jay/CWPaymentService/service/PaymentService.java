package com.jay.CWPaymentService.service;

import com.google.common.collect.Lists;
import com.jay.CWPaymentService.model.Order;
import com.jay.CWPaymentService.model.Payment;
import com.jay.CWPaymentService.model.TransactionRequest;
import com.jay.CWPaymentService.model.TransactionResponse;
import com.jay.CWPaymentService.repository.PaymentRepository;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    @Autowired
    public EmailService emailService;

    public void sendEmailDummy() throws AddressException {
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("vizagcars.wash@gmail.com"))
                .replyTo(new InternetAddress("jayanth2683@gmail.com"))
                .to(Lists.newArrayList(new InternetAddress("tlsaravind59@gmail.com")))
                .subject("Lorem ipsum")
                .body("Hey, your car has been washed "+ "\n" +" You RECEIPT: " + "\n"+" Get lost brrr brrrr")
                .encoding(String.valueOf(StandardCharsets.UTF_8)).build();

        emailService.send(email);
    }

    public void sendEmail(Payment payment, String emailAddress) throws AddressException {
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("vizagcars.wash@gmail.com"))
                .replyTo(new InternetAddress("jayanth2683@gmail.com"))
                .to(Lists.newArrayList(new InternetAddress(emailAddress)))
                .subject("Vizag Car Wash Payment-Receipt")
                .body("Hey" + payment.getCustomerName() +", your car has been washed "+ "\n" +" Your RECEIPT: " + "\n" + payment.toString())
                .encoding(String.valueOf(StandardCharsets.UTF_8)).build();

        emailService.send(email);
    }

    public TransactionResponse doPaymentSetOrderPaymentStatus(TransactionRequest request) throws AddressException {
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
            sendEmail(payment,order.getEmailAddress());
            return new TransactionResponse(order, payment.getTransactionId(), payment.getAmount(), "Payment Successful");
        } else {
            sendEmail(payment,order.getEmailAddress());
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
