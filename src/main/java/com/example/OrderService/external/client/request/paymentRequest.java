package com.example.OrderService.external.client.request;

import com.example.OrderService.model.paymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class paymentRequest {

    private Long orderId;
    private Long amount;
    private String referenceNumber;
    private paymentType PaymentType;
}
