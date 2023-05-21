package com.example.OrderService.service;

import com.example.OrderService.model.orderRequest;
import com.example.OrderService.model.orderResponse;

public interface orderService {
    orderResponse getOrderDetails(long orderId);

    long placeOrder(orderRequest orderRequest);
}
