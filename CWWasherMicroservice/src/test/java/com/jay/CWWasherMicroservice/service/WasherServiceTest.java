package com.jay.CWWasherMicroservice.service;

import com.jay.CWWasherMicroservice.model.RatingReview;
import com.jay.CWWasherMicroservice.model.Washer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WasherServiceTest {


    WasherService washerServiceMock = mock(WasherService.class);


    @Test
    @DisplayName("Get Logged In Washer Name")
    void washerName() {
        when(washerServiceMock.washerName()).thenReturn("name of the washer logged In");
    }

    @Test
    @DisplayName("Check Customer Wash-Requests")
    void washRequestFromCustomer() {
        when(washerServiceMock.washRequestFromCustomer()).thenReturn("book-wash");
    }

    @Test
    void receiveNotification() throws Exception {
        when(washerServiceMock.receiveNotification()).thenReturn("received customer notification");
        assertEquals(1,washerServiceMock.receiveNotification());
    }

    @Test
    void sendNotification() {
    }

    @Test
    @DisplayName("Get washer by name")
    void findByName() {
        when(washerServiceMock.findByName("washer"))
                .thenReturn(new Washer(
                        1,"washer","pass",new ArrayList<String>(),new ArrayList<RatingReview>()));

    }

    @Test
    void washerChoice() {
        when(washerServiceMock.washerChoice(true)).thenReturn("washer accepted the wash");
    }
}