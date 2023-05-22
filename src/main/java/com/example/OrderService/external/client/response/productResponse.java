package com.example.OrderService.external.client.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class productResponse {

    private String productName;
    private long price;
    private long quantity;
    private long productId;
}
