package com.capitole.sisu.products.application.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class CacheEvictionService {

    private final CacheManager cacheManager;

    public CacheEvictionService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Mono<Void> evictCurrentPricesCache(Long productId, Long brandId, LocalDateTime date) {
        String cacheKey = productId + "-" + brandId;
        Cache cache = cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE);
        if (cache != null) {
            cache.evict(cacheKey);
        }
        return Mono.empty();
    }
}
