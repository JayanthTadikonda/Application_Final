package com.jay.CWWasherMicroservice.service;

import com.jay.CWWasherMicroservice.model.RatingReview;
import com.jay.CWWasherMicroservice.model.Washer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WasherServiceImplTest {


    WasherServiceImpl washerServiceImplMock = mock(WasherServiceImpl.class);


    @Test
    @DisplayName("Get Logged In Washer Name")
    void washerName() {
        when(washerServiceImplMock.washerName()).thenReturn("name of the washer logged In");
    }

    @Test
    @DisplayName("Check Customer Wash-Requests")
    void washRequestFromCustomer() {
        when(washerServiceImplMock.washRequestFromCustomer()).thenReturn("book-wash");
    }

    @Test
    void receiveNotification() throws Exception {
        when(washerServiceImplMock.receiveNotification()).thenReturn("received customer notification");
        assertEquals(1, washerServiceImplMock.receiveNotification());
    }

    @Test
    void sendNotification() {
    }


    @Test
    @DisplayName("Get washer by name")
    void findByName() {
        when(washerServiceImplMock.findByName("washer"))
                .thenReturn(new Washer(
                        null,"washer","pass",new ArrayList<String>(),new ArrayList<RatingReview>(),null));

    }

    @Test
    void washerChoice() {
        when(washerServiceImplMock.washerChoice(true)).thenReturn("washer accepted the wash");
    }
}