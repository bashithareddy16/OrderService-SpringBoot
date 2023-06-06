package com.example.OrderService.service;


import com.example.OrderService.entity.order;
import com.example.OrderService.exception.CustomException;
import com.example.OrderService.external.client.paymentService;
import com.example.OrderService.external.client.productService;
import com.example.OrderService.external.client.request.paymentRequest;
import com.example.OrderService.external.client.response.paymentResponse;
import com.example.OrderService.external.client.response.productResponse;
import com.example.OrderService.model.orderRequest;
import com.example.OrderService.model.orderResponse;
import com.example.OrderService.model.paymentType;
import com.example.OrderService.repository.orderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class orderServiceImplTest {

    @Mock
    private orderRepository OrderRepository;

    @Mock
    private productService ProductService;

    @Mock
    private paymentService PaymentService;

    @Mock
    private RestTemplate restTemplate;


    @InjectMocks
    orderService OrderService = new orderServiceImpl();

    @DisplayName("Get Order - Success Scenario")
    @Test
    void test_When_Order_Success(){
        order Order = getMockOrder();

        when(OrderRepository.findById(anyLong()))
                        .thenReturn(Optional.of(Order));


        when(restTemplate.getForObject("http://PRODUCT-SERVICE/products/"  + Order.getProductId(),
        productResponse.class)).thenReturn(getMockProductResponse());


        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + Order.getOrderId(),
        paymentResponse.class)).thenReturn(getMockPaymentResponse());


        orderResponse OrderResponse = OrderService.getOrderDetails(2);


        //verification
        verify(OrderRepository, times(1)).findById(anyLong());

        verify(restTemplate , times(1)).getForObject("http://PRODUCT-SERVICE/products/"  + Order.getProductId(),
                productResponse.class);

        verify(restTemplate , times(1)).getForObject("http://PAYMENT-SERVICE/payment/order/" + Order.getOrderId(),
                paymentResponse.class);

        Assertions.assertNotNull(OrderResponse);
        assertEquals(Order.getOrderId() , OrderResponse.getOrderId());
    }

    @DisplayName("Get Orders - Failure Scenario")
    @Test
    void test_When_Get_Order_NOT_FOUND_then_Not_Found(){
        when(OrderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        CustomException exception = assertThrows(CustomException.class,
                ()-> OrderService.getOrderDetails(2));

        assertEquals("NOT_FOUND" , exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        verify(OrderRepository, times(1)).findById(anyLong());

//        orderResponse OrderResponse = OrderService.getOrderDetails(2);
    }

    @DisplayName("Place Order - Success Scenario")
    @Test
    void test_When_Place_Order_Success(){

        order Order = getMockOrder();
        orderRequest OrderRequest = getMockOrderRequest();

        when(OrderRepository.save(any(order.class))).thenReturn(Order);
        when(ProductService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(PaymentService.doPayment(any(paymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L, HttpStatus.OK));

        long orderId = OrderService.placeOrder(OrderRequest);


        verify(OrderRepository, times (2))
                .save(any());
        verify(ProductService, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(PaymentService, times(1))
                .doPayment(any(paymentRequest.class));


        assertEquals(Order.getOrderId(), orderId);
    }


    @DisplayName("Place Order - Payment Fail Scenario")
    @Test
    void test_when_Place_Order_PaymentFail_then_OrderPlaced(){
        order Order = getMockOrder();
        orderRequest OrderRequest = getMockOrderRequest();

        when(OrderRepository.save(any(order.class))).thenReturn(Order);
        when(ProductService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(PaymentService.doPayment(any(paymentRequest.class)))
                .thenThrow(new RuntimeException());

        long orderId = OrderService.placeOrder(OrderRequest);

        verify(OrderRepository, times (2))
                .save(any());
        verify(ProductService, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(PaymentService, times(1))
                .doPayment(any(paymentRequest.class));

        assertEquals(Order.getOrderId(), orderId);

    }
    private orderRequest getMockOrderRequest() {
        return orderRequest.builder()
                .productId(1)
                .quantity(2)
                .PaymentType(paymentType.CASH)
                .amount(1234)
        .build();

    }

    private order getMockOrder(){
        return order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .orderId(2)
                .amount(1234)
                .quantity(2)
                .productId(2)
                .build();
    }

    private productResponse getMockProductResponse(){
        return productResponse.builder()
                .productId(1)
                .productName("iphone")
                .price(1234)
                .quantity(2)
                .build();
    }

    private paymentResponse getMockPaymentResponse(){
        return paymentResponse.builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .PaymentType(paymentType.PAY_PAL)
                .amount(1234)
                .orderId(2)
                .paymentStatus("ACCEPTED")
                .build();
    }
}