package com.capitole.sisu.products.port.out.out;

import com.capitole.sisu.products.domain.price.CurrentPrice;
import com.capitole.sisu.products.domain.price.Price;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface DatabasePriceInPort extends DatabasePort<Price> {
    Mono<CurrentPrice> getCurrentPriceByProductAndBrand(Long productId, Long brandId, LocalDateTime date);

    Flux<Price> findAllByProductIdAndBrandId(Long productId, Long brandId);
}
