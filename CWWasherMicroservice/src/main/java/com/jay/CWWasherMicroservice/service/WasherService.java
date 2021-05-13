package com.jay.CWWasherMicroservice.service;

import com.jay.CWWasherMicroservice.model.Order;
import com.jay.CWWasherMicroservice.model.RatingReview;
import com.jay.CWWasherMicroservice.model.Washer;
import com.jay.CWWasherMicroservice.model.WasherLeaderboard;

import java.util.List;

public interface WasherService {

    public String washRequestFromCustomer();

    public String receiveNotification() throws Exception;

    public void sendNotification(String notification);

    public Washer findByName(String name);

    public String washerChoice(Boolean option);

    public RatingReview takeRating(RatingReview ratingReview);

    public List<Order> washerOrders(String name);

    public List<WasherLeaderboard> washerLeaderboard();
}
