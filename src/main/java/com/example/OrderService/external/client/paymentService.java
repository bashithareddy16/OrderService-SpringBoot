package com.example.OrderService.external.client;

import com.example.OrderService.external.client.request.paymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE/payment")

public interface paymentService {


    @PostMapping
    ResponseEntity<Long> doPayment(@RequestBody paymentRequest PaymentRequest);
}
