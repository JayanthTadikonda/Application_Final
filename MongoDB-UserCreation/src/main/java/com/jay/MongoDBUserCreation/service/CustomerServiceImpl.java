package com.jay.MongoDBUserCreation.service;

import com.jay.MongoDBUserCreation.exceptions.AllFieldsAreNecessaryException;
import com.jay.MongoDBUserCreation.exceptions.CustomerNotFoundException;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"UnusedDeclaration"})
public class CustomerServiceImpl implements CustomerService {

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

    public WashPack getPack(String packName) {
        return restTemplate.getForObject("http://admin/admin/get-pack/" + packName, WashPack.class);
    }

    public String washBookingResponseFromWasher() {
        return washerResponse;
    }

    public Customer findByName(String name) {

        Customer customer = customerRepository
                .findAll()
                .stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findAny().orElse(null);
        if (customer == null) {
            throw new CustomerNotFoundException("Sorry, Customer with the provided name not found, please provide the name used while registration !");
        } else return customer;
    }

    public Customer updateProfile(String name) {

        Customer customer1 = customerRepository.findByName(jwtFilter.getLoggedInUserName());
        customer1.setName(name);
        return customerRepository.save(customer1);
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
            if (!orderId_paymentList.contains(o.getOrderId()) && washStatus.contains("wash-completed")) {
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
            sendNotification("Order placed at: " + order.getDate() + "with Washer Partner: " + resp.substring(40));
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

    public List<Order> customerOrders(String name) {

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

    public List<WasherLeaderboard> washerLeaderboard() {
        List<WasherLeaderboard> washerLeaderboardList = null;

        try {
            ResponseEntity<List<WasherLeaderboard>> claimResponse = restTemplate.exchange(
                    "http://admin/admin/leaderboard",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<WasherLeaderboard>>() {
                    });
            if (claimResponse.hasBody()) {
                washerLeaderboardList = claimResponse.getBody();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return washerLeaderboardList;
    }

    public AddOn getAddOn(String name) {

        return restTemplate.getForObject("http://admin/admin/get-addOn/" + name, AddOn.class);
    }

    public List<WashPack> getAllWashPackages() {
        ResponseEntity<List<WashPack>> packs =
                restTemplate.exchange("http://admin/admin/all-packs",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<WashPack>>() {
                        });
        List<WashPack> packList = packs.getBody();
        return packList;
    }

    public List<AddOn> getAllAddOns(){
        ResponseEntity<List<AddOn>> packs =
                restTemplate.exchange("http://admin/admin/all-addOns",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<AddOn>>() {
                        });
        List<AddOn> addOnList = packs.getBody();
        return addOnList;
    }

    public Customer addNewCustomer(Customer customer) {
        if (customer == null) {
            throw new AllFieldsAreNecessaryException("Fill in Complete Details");
        } else {
            customer.setRole("CUSTOMER");
            return customerRepository.save(customer);
        }
    }
}
