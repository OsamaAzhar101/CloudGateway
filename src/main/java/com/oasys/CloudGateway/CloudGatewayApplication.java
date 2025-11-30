package com.oasys.CloudGateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class CloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayApplication.class, args);
    }


/*    @org.springframework.context.annotation.Bean
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
    }*/

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just("userKey");
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        return factory -> factory.configureDefault(id ->
                new Resilience4JConfigBuilder(
                        id)
                        .circuitBreakerConfig(
                                CircuitBreakerConfig.ofDefaults()

                        ).build()
        );
    }


}
