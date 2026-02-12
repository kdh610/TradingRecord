package com.tradingRecord.tradingRecord.infrastructure.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {
    @Bean
    public Bucket kiwoomApiBucket(){
        return Bucket.builder()
                .addLimit(
                        Bandwidth.builder()
                                .capacity(5)
                                .refillGreedy(5, Duration.ofSeconds(1)) // 1초에 5개
                                .build()
                ).build();
    }
}
