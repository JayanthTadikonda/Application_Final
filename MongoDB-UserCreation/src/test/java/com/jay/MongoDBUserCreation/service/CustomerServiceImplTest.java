package com.jay.MongoDBUserCreation.service;

import com.jay.MongoDBUserCreation.model.Customer;
import com.jay.MongoDBUserCreation.model.WashPack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    CustomerServiceImpl customerServiceImplMock = mock(CustomerServiceImpl.class);

    @Test
    @DisplayName("Get the list of Wash Packages")
    void getPacks() {
        List<WashPack> washPackList = Arrays.asList(new WashPack("one",23),
                new WashPack("one",2234),
                new WashPack("interior",673),
                new WashPack("engine",55));
        when(customerServiceImplMock.getPacks()).thenReturn(washPackList);
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
                1,"a","pass",new ArrayList<String>(),"suv","email",null);
        when(customerServiceImplMock.findByName("customer")).thenReturn(customer);
    }

    @Test
    @DisplayName("Sending washer the wash request/booking wash")
    void sendNotification() {
        when(customerServiceImplMock.sendNotification("book-wash")).thenReturn("wash request sent");
    }


    @Test
    void doPay() {
    }

    @Test
    void payAfterWash() {
    }

    @Test
    void placeOrder() {
    }

    @Test
    void giveRatingAndReview() {
    }
}