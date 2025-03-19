package com.capitole.sisu.products.application.service;

import com.capitole.sisu.products.application.cache.CacheConstants;
import com.capitole.sisu.products.application.cache.CacheEvictionService;
import com.capitole.sisu.products.domain.price.*;
import com.capitole.sisu.products.port.in.rest.RestPriceInPort;
import com.capitole.sisu.products.port.out.DatabasePriceInPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PriceService implements RestPriceInPort {
    private final DatabasePriceInPort databasePriceInPort;
    private final PriceManager priceManager;
    private final CacheManager cacheManager;
    private final CacheEvictionService cacheEvictionService;

    public PriceService(DatabasePriceInPort databasePriceInPort,
                        PriceManager priceManager,
                        CacheManager cacheManager,
                        CacheEvictionService cacheEvictionService) {
        this.databasePriceInPort = databasePriceInPort;
        this.priceManager = priceManager;
        this.cacheManager = cacheManager;
        this.cacheEvictionService = cacheEvictionService;
    }

    @Override
    public Mono<Price> create(Price price) {
        Flux<Price> prices =
                databasePriceInPort.findAllByProductIdAndBrandId(price.getProductId(), price.getBrandId());

        return this.priceManager
                .doesPriceOverlap(prices, price)
                .flatMap(
                        overlapping -> {
                            if (overlapping) {
                                return Mono.error(
                                        new PriceOverlappingException("Price overlaps with an existing price."));
                            }
                            return databasePriceInPort.save(price);
                        })
                .switchIfEmpty(Mono.defer(() -> databasePriceInPort.save(price)));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return databasePriceInPort
                .findById(id)
                .switchIfEmpty(Mono.error(new PriceNotFoundException(String.format("Price with ID %d not found.", id))))
                .flatMap(existingPrice -> databasePriceInPort.delete(id)
                        .then(Mono.fromRunnable(() ->
                                this.cacheEvictionService.evictCurrentPricesCache(
                                        existingPrice.getProductId(),
                                        existingPrice.getBrandId(),
                                        existingPrice.getStartDate())
                        ))
                );
    }

    @Override
    public Mono<Page<Price>> findAll(Pageable pageable) {
        return databasePriceInPort.findAll(pageable);
    }

    @Override
    public Mono<CurrentPrice> getCurrentPrice(Long productId, Long brandId, LocalDateTime date) {
        Cache cache = cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE);
        if (cache == null) {
            return Mono.error(new IllegalStateException("Cache not found"));
        }

        String cacheKey = productId + "-" + brandId;

        TreeMap<LocalDateTime, CurrentPrice> cachedPrices = cache.get(cacheKey, TreeMap.class);
        if (cachedPrices != null) {
            CurrentPrice cachedPrice = getCurrentPriceInCache(cachedPrices, date);
            if (cachedPrice != null) {
                return Mono.just(cachedPrice);
            }
        }

        return getCurrentPriceFromDatabase(productId, brandId, date, cachedPrices, cache, cacheKey);
    }

    private Mono<CurrentPrice> getCurrentPriceFromDatabase(Long productId, Long brandId, LocalDateTime date, TreeMap<LocalDateTime, CurrentPrice> cachedPrices, Cache cache, String cacheKey) {
        return databasePriceInPort.getCurrentPriceByProductAndBrand(productId, brandId, date)
                .switchIfEmpty(
                        Mono.error(new PriceNotFoundException("No price found for the given product and brand."))
                )
                .flatMap(price -> {
                    TreeMap<LocalDateTime, CurrentPrice> prices = cachedPrices != null ? cachedPrices : new TreeMap<>();
                    prices.put(price.getStartDate(), price);
                    cache.put(cacheKey, prices);

                    return Mono.just(price);
                });
    }

    private CurrentPrice getCurrentPriceInCache(TreeMap<LocalDateTime, CurrentPrice> cachedPrices, LocalDateTime date) {
        Map.Entry<LocalDateTime, CurrentPrice> entry = cachedPrices.floorEntry(date);
        if (entry == null) {
            return null;
        }

        CurrentPrice price = entry.getValue();
        if (!date.isBefore(price.getStartDate()) && !date.isAfter(price.getEndDate())) {
            return price;
        }

        return null;
    }
}
