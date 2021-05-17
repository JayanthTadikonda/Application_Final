package com.jay.MongoDBUserCreation.service;

import com.jay.MongoDBUserCreation.model.*;

import java.util.List;

public interface CustomerService {


    public WashPack getPack(String packName);

    public String washBookingResponseFromWasher();

    public Customer findByName(String name);

    public Customer updateProfile(String name);

    public String sendNotification(String notification);

    public String receiveNotification() throws Exception;

    public TransactionResponse doPay(Order order, RatingReview ratingReview);

    public TransactionResponse payAfterWash(RatingReview ratingReview) throws Exception;

    public OrderResponse placeOrder(String packName, String addOn) throws Exception;

    public RatingReview giveRatingAndReview(RatingReview ratingReview);

    public List<Order> customerOrders(String name);

    public List<WasherLeaderboard> washerLeaderboard();

    public AddOn getAddOn(String name);

    public List<WashPack> getAllWashPackages();

    public Customer addNewCustomer(Customer customer);

}
