package com.jay.CWPaymentService.service;

import com.jay.CWPaymentService.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.mail.internet.AddressException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    PaymentService paymentServiceMock = mock(PaymentService.class);


    @Test
    @DisplayName("first car wash test ever")
    void sendEmailDummy() {
        assertTrue(true);
    }

    @Test
    void sendEmail() throws AddressException {
//        assertEquals("MailSent",paymentServiceMock.sendEmailDummy());
    }

    @Test
    void paymentProcessing() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Access Payment List of a Customer")
    void testPaymentListWithMock() {


        List<Payment> paymentList = Arrays.asList(new Payment(null,2,"a","b","success","444",3223,999,null,"good",3),
                new Payment(null,2,"a","b","success","5434",3223,999,null,"good",3),
                new Payment(null,2,"a","b","success","756",3223,999,null,"good",3));

        when(paymentServiceMock.paymentList("customer")).thenReturn(paymentList);

    }
}