package com.example.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class orderResponse {
    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;
    private productDetails ProductDetails;
    private paymentDetails PaymentDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class productDetails{

        private String productName;
        private long price;
        private long quantity;
        private long productId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class paymentDetails {
        private long paymentId;
        private String paymentStatus;
        private paymentType PaymentType;
        private long amount;
        private Instant paymentDate;
        private long orderId;
    }
}
