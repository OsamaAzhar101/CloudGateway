package com.oasys.CloudGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudGatewayApplication.class, args);
	}


    @org.springframework.context.annotation.Bean
    public org.springframework.cloud.client.circuitbreaker.Customizer<org.springframework.
            cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder> resilience4JCircuitBreakerCustomizer() {
        return builder -> builder
                .timeLimiterConfig(io.github.resilience4j.timelimiter.TimeLimiterConfig.custom()
                        .timeoutDuration(java.time.Duration.ofSeconds(2)).build())
                .circuitBreakerConfig(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(java.time.Duration.ofSeconds(30))
                        .build());
    }
}
