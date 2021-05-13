package com.jay.MongoDBUserCreation.service;

import com.jay.MongoDBUserCreation.filter.JwtFilter;
import com.jay.MongoDBUserCreation.model.*;
import com.jay.MongoDBUserCreation.repository.CustomerRepository;
import com.jay.MongoDBUserCreation.util.JwtUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"UnusedDeclaration"})
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    private static final String request = "request-booking";
    private static final String booking = "booking-queue";

    String washerResponse = "";

    private final List<WashPack> packs = new ArrayList<>(Arrays.asList(new WashPack("basic-wash", 999),
            new WashPack("advanced-wash", 1999)));

    private final List<AddOn> addOnList = new ArrayList<>(Arrays.asList(new AddOn("interior-clean",499),
            new AddOn("Sanitization",599),
            new AddOn("Teflon-Coating",699),
            new AddOn("Engine-Care",799)));


    public List<WashPack> getPacks() {
        return packs;
    }

    public WashPack getPack(String packName) {
        return packs.stream().filter(p -> p.getPackName().contains(packName)).findAny().orElse(null);
    }

    public String washBookingResponseFromWasher() {
        return washerResponse;
    }

    public Customer findByName(String name) {

        return customerRepository
                .findAll()
                .stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }

    public String updateProfile(String name, Customer customer) {
        if (customerRepository.findByName(name).getName().equalsIgnoreCase(name)) {
            Customer customer1 = customerRepository.findByName(name);
            customer1.setName(customer.getName());
            customer1.setPassword(customer.getPassword());
            customer1.setAddress(customer.getAddress());
            customer1.setCarModel(customer.getCarModel());
            customerRepository.save(customer);
        } else
            return "No Customer by that name:" + name;
        return name + " Profile Updated";
    }

    public String sendNotification(String notification) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(request, false, false, false, null);
            channel.basicPublish("", request, null, notification.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent Message is: " + notification);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "Wash Request Sent, By: " + customerRepository.findByName(jwtFilter.getLoggedInUserName()).toString();
    }

    public String receiveNotification() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(booking, false, false, false, null);
        System.out.println("Waiting for messages from Sender, ctrl+c to quit");

        DeliverCallback deliverCallback = (ct, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            washerResponse = msg;
            System.out.println("Received Message is: " + msg);

        };
        channel.basicConsume(booking, true, deliverCallback, c -> {
        });
        return washerResponse;
    }

    public TransactionResponse doPay(Order order, RatingReview ratingReview) {
        Payment payment = new Payment();
        payment.setCustomerName(order.getCustomerName());
        payment.setWasherName(order.getWasherName());
        payment.setOrderId(order.getOrderId());
        payment.setAmount(order.getAmount());
        payment.setReview(ratingReview.getReview());
        payment.setRating(ratingReview.getRating());
        TransactionRequest request = new TransactionRequest(order, payment);
        sendNotification("Payment Processed with ID:" + payment.getTransactionId());
        return restTemplate.postForObject("http://payment-microservice/payment/pay", request, TransactionResponse.class);
    }

    public TransactionResponse payAfterWash(RatingReview ratingReview) throws Exception {

        Payment payment = new Payment();
        Customer customer = customerRepository.findByName(jwtFilter.getLoggedInUserName());

        List<Payment> paymentList = null;
        List<Order> orderList = null;
        TransactionResponse response = new TransactionResponse();
        TransactionResponse finalResponse = new TransactionResponse();

        try {
            ResponseEntity<List<Payment>> claimResponse = restTemplate.exchange(
                    "http://payment-microservice/payment/get-payment/" + customer.getName(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Payment>>() {
                    });
            if (claimResponse.hasBody()) {
                paymentList = claimResponse.getBody();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        try {
            ResponseEntity<List<Order>> claimResponse = restTemplate.exchange(
                    "http://order-microservice/order/get-orders/" + customer.getName(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Order>>() {
                    });
            if (claimResponse.hasBody()) {
                orderList = claimResponse.getBody();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        assert paymentList != null;
        List<Integer> orderId_paymentList = paymentList.stream().map(Payment::getOrderId).collect(Collectors.toList());
        //if (washBookingResponseFromWasher().contains("wash-completed")) {
        String washStatus = receiveNotification();
        assert orderList != null;
        for (Order o : orderList) {
            //Condition for FIRST-TIME payment
            if (!orderId_paymentList.contains(o.getOrderId())) {
                //Do 1st Time payment
                return doPay(o, ratingReview);
            } else if (!orderId_paymentList.contains(o.getOrderId()) && o.getPaymentStatus().contains("pending")) {
                return doPay(o, ratingReview);
            }
        }
        return new TransactionResponse(finalResponse.getOrder(), finalResponse.getTransactionId(), finalResponse.getAmount(), "All Payments Successful, No Pending Payments");
    }

    public OrderResponse placeOrder(String packName, String addOn) throws Exception {
        Order placedOrder;
        String resp = receiveNotification();

        if (resp.contains("accepted-wash-request")) {
            resp = washBookingResponseFromWasher();
            Customer customer = customerRepository.findByName(jwtFilter.getLoggedInUserName());
            Order order = new Order();
            order.setCustomerName(jwtFilter.getLoggedInUserName());
            order.setCarModel(customer.getCarModel());
            order.setWasherName(resp.substring(40));
            order.setWashName(getPack(packName).getPackName());
            order.setAddOn(getAddOn(addOn));
            order.setAmount(getPack(packName).getAmount() + getAddOn(addOn).getAmount());
            order.setDate(new Date(System.currentTimeMillis()));
            order.setEmailAddress(customer.getEmailAddress());
            placedOrder = restTemplate.postForObject("http://order-microservice/order/place-order", order, Order.class);
            sendNotification("Order placed at: " + order.getDate() + "with Washer Partner: " + order.getWasherName());
            String orderStatus = restTemplate.getForObject("http://washer-microservice/washer/order-accepted", String.class);
            System.out.println(orderStatus);

        } else
            return new OrderResponse(null, "Order is already Placed !");

        return new OrderResponse(placedOrder, "Order for Wash is placed with Washer Partner");
    }

    public RatingReview giveRatingAndReview(RatingReview ratingReview) {
        //ratingReview.setWasherName();
        return restTemplate.postForObject("http://washer-microservice/washer/get-rating", ratingReview, RatingReview.class);
    }

    public List<Order> customerOrders(String name){

        List<Order> orderList = null;

        try {
            ResponseEntity<List<Order>> claimResponse = restTemplate.exchange(
                    "http://order-microservice/order/get-orders/" + name,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Order>>() {
                    });
            if (claimResponse.hasBody()) {
               orderList = claimResponse.getBody();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public List<WasherLeaderboard> washerLeaderboard(){
        return restTemplate.getForObject("http://washer-microservice/washer/washer-leaderboard",List.class);
    }

    public AddOn getAddOn(String name){
        return addOnList.stream().filter(a->a.getAddOnName().contains(name)).findAny().orElse(null);
    }
}
