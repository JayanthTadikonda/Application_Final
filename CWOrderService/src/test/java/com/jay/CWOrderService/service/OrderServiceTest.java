package com.jay.CWOrderService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jay.CWOrderService.model.Order;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    OrderService orderServiceMock = mock(OrderService.class);


    @Test
    void saveOrder() {
    }

    @Test
    void payAfterWash() throws JsonProcessingException {
        List<Order> orderList = Arrays.asList(
                new com.jay.CWOrderService.model.Order(
                        null,1,"asd","suv",999,"Kevin","sds",null,null,"paid","mail"));

        when(orderServiceMock.payAfterWash(orderList.get(0))).thenReturn(orderList.get(0));
    }

    @Test
    void getOrderListByName() {

        List<Order> orderList = Arrays.asList(
                new com.jay.CWOrderService.model.Order(
                        null,1,"asd","suv",999,"Kevin","sds",null,null,"paid","mail"));


        when(orderServiceMock.getOrderListByName("kevin")).thenReturn(orderList);
    }
}