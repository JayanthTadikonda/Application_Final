package com.jay.MongoDBUserCreation.service;

import com.jay.MongoDBUserCreation.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    CustomerServiceImpl customerServiceImplMock = mock(CustomerServiceImpl.class);

    Order orderMock = mock(Order.class);

    private final List<WashPack> packs = new ArrayList<>(Arrays.asList(new WashPack("basic-wash", 999),
            new WashPack("advanced-wash", 1999)));

    private final List<AddOn> addOnList = new ArrayList<>(Arrays.asList(new AddOn("interior-clean",499),
            new AddOn("Sanitization",599),
            new AddOn("Teflon-Coating",699),
            new AddOn("Engine-Care",799)));

    @Test
    @DisplayName("Get the list of Wash Packages")
    void getPacks() {
        List<WashPack> washPackList = Arrays.asList(new WashPack("one",23),
                new WashPack("one",2234),
                new WashPack("interior",673),
                new WashPack("engine",55));
        when(customerServiceImplMock.getAllWashPackages()).thenReturn(washPackList);
    }

    @Test
    @DisplayName("Get wash pack by Name")
    void getPack() {
        List<WashPack> washPackList = Arrays.asList(new WashPack("one",23),
                new WashPack("one",2234),
                new WashPack("interior",673),
                new WashPack("engine",55));
        when(customerServiceImplMock.getPack("one")).thenReturn(washPackList.get(1));
    }

    @Test
    @DisplayName("Washer reply")
    void washBookingResponseFromWasher() {
        when(customerServiceImplMock.washBookingResponseFromWasher()).thenReturn("accepted/rejected");
    }

    @Test
    @DisplayName("Find customer by Name")
    void findByName() {
        Customer customer = new Customer(
                1,"a","pass",new ArrayList<String>(),"suv","email","CUSTOMER");
        when(customerServiceImplMock.findByName("customer")).thenReturn(customer);
    }

    @Test
    @DisplayName("Sending washer the wash request/booking wash")
    void sendNotification() {
        when(customerServiceImplMock.sendNotification("book-wash")).thenReturn("wash request sent");
    }


    @Test
    @DisplayName("Pay after wash with  ratings")
    void doPay() {
        Order order = new Order(
                1,"basic","suv",1234,
                "customer1","washer1",new AddOn("sanitize",599),new Date(),"success","email");
        when(customerServiceImplMock.doPay(order,new RatingReview("good",5))).thenReturn(
                new TransactionResponse(order,order.getDate().toString(),order.getAmount(),"Testing Do-Pay"));
    }

    @Test
    @DisplayName("Pay, with ratings")
    void doPayWithoutAddOn() {
        Order order = new Order(
                1,"basic","suv",1234,
                "customer1","washer1",null,new Date(),"success","email@email.com");
        when(customerServiceImplMock.doPay(order,new RatingReview("good",5))).thenReturn(
                new TransactionResponse(order,order.getDate().toString(),order.getAmount(),"Testing Do-Pay"));
    }

    @Test
    @DisplayName("Pay after wash without ratings")
    void doPayWithoutRating() {
        Order order = new Order(
                1,"basic","suv",1234,
                "customer1","washer1",new AddOn("sanitize",599),new Date(),"success","email");
        when(customerServiceImplMock.doPay(order,null)).thenReturn(
                new TransactionResponse(order,order.getDate().toString(),order.getAmount(),"Testing Do-Pay"));
    }

    @Test
    @DisplayName("Pay without ratings and AddOn")
    void doPayWithoutRatingAndAddOn() {
        Order order = new Order(
                1,"basic","suv",1234,
                "customer1","washer1",null,new Date(),"success","email@email.com");
        when(customerServiceImplMock.doPay(order,null)).thenReturn(
                new TransactionResponse(order,order.getDate().toString(),order.getAmount(),"Testing Do-Pay"));
    }


    @Test
    @DisplayName("Test Pay After Wash with Transaction Response")
    void payAfterWash() throws Exception {
        RatingReview ratingReview = new RatingReview("good",5);
        Order order = new Order(
                1,"basic","suv",1234,
                "customer1","washer1",null,new Date(),"success","email@email.com");
        when(customerServiceImplMock.payAfterWash(ratingReview)).thenReturn(new TransactionResponse(order,order.getDate().toString(),order.getAmount(),"Testing Do-Pay"));
    }

    @Test
    @DisplayName("Placing Order with Wash-Pack Only")
    void placeOrder() throws Exception {
        Order order = new Order(
                1,"basic","suv",1234,
                "customer1","washer1",null,new Date(),"success","email@email.com");
        when(customerServiceImplMock.placeOrder("basic",null)).thenReturn(new OrderResponse(order,"placed"));
    }

    @Test
    @DisplayName("Placing Order with Wash-Pack and AddOn")
    void placeOrderWithAddOn() throws Exception {
        Order order = new Order(
                1,"basic","suv",1234,
                "customer1","washer1",new AddOn("sanitize",499),new Date(),"success","email@email.com");
        when(customerServiceImplMock.placeOrder("basic",null)).thenReturn(new OrderResponse(order,"placed"));
    }

    @Test
    @DisplayName("Testing Rating - Review")
    void giveRatingAndReview() {
        when(customerServiceImplMock.giveRatingAndReview(new RatingReview("good",5))).thenReturn(new RatingReview("good",5));
    }

    @Test
    @DisplayName("Testing Customer skip Rating")
    void giveRatingAndReviewNull() {
        when(customerServiceImplMock.giveRatingAndReview(null)).thenReturn(null);
    }

    @Test
    void washerLeaderboard() {
        when(customerServiceImplMock.washerLeaderboard()).thenReturn(new ArrayList<>());
    }

    @Test
    void getAddOn() {
        when(customerServiceImplMock.getAddOn("sanitize")).thenReturn(new AddOn("sanitize",399));
    }

    @Test
    void getAllWashPackages() {
        when(customerServiceImplMock.getAllWashPackages()).thenReturn(new ArrayList<>());
    }
}