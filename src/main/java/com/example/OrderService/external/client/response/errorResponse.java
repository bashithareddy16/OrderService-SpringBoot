package com.example.OrderService.external.client.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class errorResponse {

    private String errorMessage;
    private String errorCode;
}
