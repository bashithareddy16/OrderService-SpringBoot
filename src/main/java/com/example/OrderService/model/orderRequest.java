package com.example.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class orderRequest {

    private long productId;
    private long amount;
    private long quantity;
    private paymentType PaymentType;
    
}
