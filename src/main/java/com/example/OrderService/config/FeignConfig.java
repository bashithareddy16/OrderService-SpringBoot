package com.example.OrderService.config;

import com.example.OrderService.external.client.decoder.customErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    ErrorDecoder errorDecoder() {
        return new customErrorDecoder();
    }
}
