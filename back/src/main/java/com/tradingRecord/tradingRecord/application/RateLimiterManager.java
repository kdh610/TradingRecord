package com.tradingRecord.tradingRecord.application;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimiterManager {
    private final Bucket kiwoomApiBucket;

    public boolean tryConsume() {
        return kiwoomApiBucket.tryConsume(1);
    }

    public ConsumptionProbe probe() {
        return kiwoomApiBucket.tryConsumeAndReturnRemaining(1);
    }

}
