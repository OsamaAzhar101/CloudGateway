package com.oasys.CloudGateway.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @PostMapping("/order-fallback")
    public ResponseEntity<Map<String, String>> orderServiceFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order Service is currently unavailable. Please try again later.");
        response.put("status", "SERVICE_UNAVAILABLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
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
