package com.jay.CWWasherMicroservice.service;

import com.jay.CWWasherMicroservice.filter.JwtFilter;
import com.jay.CWWasherMicroservice.model.*;
import com.jay.CWWasherMicroservice.repository.WasherRepository;
import com.jay.CWWasherMicroservice.util.JwtUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class WasherService {

    private static final String request = "request-booking";
    private static final String booking = "booking-queue";

    @Autowired
    private WasherRepository washerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthenticationManager authenticationManager;

    String copyMsg = ""; //notification received from the customer

    private List<WasherLeaderboard> leaderboard = Arrays.asList(new WasherLeaderboard("washer8",1500),
            new WasherLeaderboard("washer1",834),
            new WasherLeaderboard("washer2",605),
            new WasherLeaderboard("washer3",18),
            new WasherLeaderboard("washer4",87),
            new WasherLeaderboard("washer5",459),
            new WasherLeaderboard("washer6",1453),
            new WasherLeaderboard("washer7",102));

    String washerName;
    //Logged in Washer-Name
    public String washerName(){
        washerName = jwtFilter.getLoggedInUserName();
        return jwtFilter.getLoggedInUserName();
    }

    //Notification received from the customer
    public String washRequestFromCustomer() {
        return copyMsg;
    }

    // to receive notification from the customers
    public String receiveNotification() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(request, false, false, false, null);
        System.out.println("Waiting for messages from Sender, ctrl+c to quit");


        DeliverCallback deliverCallback = (ct, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received Message is: " + msg);
            copyMsg = msg;
        };

        channel.basicConsume(request, true, deliverCallback, c -> {
        });

        return "Received Wash Request !" + copyMsg;
    }

    //Send notifications to the customers
    public void sendNotification(String notification) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(booking, false, false, false, null);
            channel.basicPublish("", booking, null, notification.getBytes(StandardCharsets.UTF_8));
            System.out.println("Sent Message is: " + notification);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Washer findByName(String name) {

        return washerRepository
                .findAll()
                .stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }

    public String washerChoice(Boolean option) {
        if (washRequestFromCustomer().contains("book-wash") && option) {
            sendNotification("accepted-wash-request by Washer Partner:" + jwtFilter.getLoggedInUserName());
            return "Wash Booking Accepted for Customer: " + washRequestFromCustomer().substring(22);
        } else
            return "Wash Booking Rejected !, Try again ";
    }

    public RatingReview takeRating(RatingReview ratingReview) {
        Washer washer = washerRepository.findByName(jwtFilter.getLoggedInUserName());
        List<RatingReview> ratingReviewList = new ArrayList<>();
        ratingReviewList.add(ratingReview);
        washer.setRatingReviewList(ratingReviewList);
        washerRepository.save(washer);
        return ratingReview;
    }

    public List<Order> washerOrders(String name){

        List<Order> orderList = null;

        try {
            ResponseEntity<List<Order>> claimResponse = restTemplate.exchange(
                    "http://order-microservice/order/washer-orders/" + name,
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
        return leaderboard;
    }

}
