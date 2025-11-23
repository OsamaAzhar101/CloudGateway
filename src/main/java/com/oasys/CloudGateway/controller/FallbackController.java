package com.oasys.CloudGateway.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping(path = "/order-service-fallback", produces = "application/json")
    public Map<String, String> orderServiceFallback() {
        return Collections.singletonMap("message", "Order Service is currently unavailable. Please try again later.");
    }

    @GetMapping(path = "/payment-service-fallback", produces = "application/json")
    public Map<String, String> paymentServiceFallback() {
        return Collections.singletonMap("message", "Payment Service is currently unavailable. Please try again later.");
    }

    @GetMapping(path = "/product-service-fallback", produces = "application/json")
    public Map<String, String> productServiceFallback() {
        return Collections.singletonMap("message", "Product Service is currently unavailable. Please try again later.");
    }
}
