package com.capitole.sisu.products.port.in.rest;

import com.capitole.sisu.products.domain.price.CurrentPrice;
import com.capitole.sisu.products.domain.price.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface RestPriceInPort extends RestPort<Price> {

    Mono<CurrentPrice> getCurrentPrice(Long productId, Long brandId, LocalDateTime date);

    Mono<Page<Price>> findAll(Pageable pageable);
}
