package com.example.OrderService.controller;

import com.example.OrderService.model.orderRequest;
import com.example.OrderService.model.orderResponse;
import com.example.OrderService.service.orderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2

public class orderController {
    @Autowired
    private orderService OrderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody orderRequest OrderRequest) {
        long orderID = OrderService.placeOrder(OrderRequest);
        log.info("Order added with , " + orderID);
        return new ResponseEntity<>(orderID, HttpStatus.OK);
    }
        @GetMapping("/{orderId}")
        public ResponseEntity<orderResponse> getOrderDetails(@PathVariable long orderId){
        orderResponse OrderResponse = OrderService.getOrderDetails(orderId);
        return new ResponseEntity<>(OrderResponse, HttpStatus.OK);
    }
}
