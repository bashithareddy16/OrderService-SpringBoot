package com.example.OrderService.service;

import com.example.OrderService.entity.order;
import com.example.OrderService.external.client.response.paymentResponse;

import com.example.OrderService.exception.CustomException;
import com.example.OrderService.external.client.paymentService;
import com.example.OrderService.external.client.productService;
import com.example.OrderService.external.client.request.paymentRequest;
import com.example.OrderService.model.orderRequest;
import com.example.OrderService.model.orderResponse;
import com.example.OrderService.repository.orderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class orderServiceImpl implements orderService{
    @Autowired
    private orderRepository OrderRepository;

    @Autowired
    private productService ProductService;

    @Autowired
    private paymentService PaymentService;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public orderResponse getOrderDetails(long orderId) {
        log.info("Get order details of order Id : {} ", orderId);
        order Order = OrderRepository.findById(orderId)
                .orElseThrow(()->new CustomException("Order not found with order ID: {}", "NOT_FOUND",404));
        log.info("Getting payment information from the payment Service");

        orderResponse OrderResponse = orderResponse.builder()
                .orderId(Order.getOrderId())
                .orderStatus(Order.getOrderStatus())
                .amount(Order.getAmount())
                .orderDate(Order.getOrderDate())
                .build();

        return OrderResponse;
    }

    @Override
    public long placeOrder(orderRequest orderRequest) {
        log.info("Placing order request" + orderRequest);

        ProductService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("Creating order with Status CREATED");

        order Order = order.builder()
                .amount(orderRequest.getAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .build();
        Order = OrderRepository.save(Order);

        log.info("Calling Payment Service to complete the payment");
        paymentRequest PaymentRequest = paymentRequest.builder()
                .orderId(Order.getOrderId())
                .paymentType(orderRequest.getPaymentType())
                .amount(orderRequest.getAmount())
                .build();
        String orderStatus = null;
        try{
            PaymentService.doPayment(PaymentRequest);
            log.info("Ordered placed successfully. Changing the order status to PLACED");
            orderStatus = "PLACED";

        } catch (Exception e){
            log.error("Error Occurred in payment. Changing order status to FAILED");
            orderStatus = "FAILED";
        }

        Order.setOrderStatus(orderStatus);
        OrderRepository.save(Order);

        log.info("Order placed successfully with order ID" + Order.getOrderId());
        return Order.getOrderId();
    }
}
