package com.reactiverest.studentapp.services;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Data
@Service
public class RateLimitService {

    // Rate limiter registry and rate limiter setup with custom config
    RateLimiterRegistry rateLimiterRegistry;
    RateLimiter rateLimiter;

    public RateLimitService() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(5))
                .limitForPeriod(3)
                .timeoutDuration(Duration.ofMillis(25))
                .build();
        // Create registry
        this.rateLimiterRegistry = RateLimiterRegistry.of(config);
        this.rateLimiter = rateLimiterRegistry
                .rateLimiter("basic", config);
    }

}
