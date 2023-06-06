package com.example.OrderService.controller;


import com.example.OrderService.orderServiceConfig;
import com.example.OrderService.repository.orderRepository;
import com.example.OrderService.service.orderService;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest({"serve.port = 0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration( classes = {orderServiceConfig.class})
public class orderControllerTest {

    @Autowired
    private orderService OrderService;


    @Autowired
    private orderRepository OrderRepository;


    @Autowired
    private MockMvc mockMvc;

    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(8080)).build();

}