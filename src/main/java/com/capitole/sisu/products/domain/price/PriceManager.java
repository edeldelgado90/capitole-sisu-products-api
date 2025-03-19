package com.capitole.sisu.products.domain.price;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PriceManager {
    Mono<Boolean> doesPriceOverlap(Flux<Price> existingPrices, Price newPrice);
}
