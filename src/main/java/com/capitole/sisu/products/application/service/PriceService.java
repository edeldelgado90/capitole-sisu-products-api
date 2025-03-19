package com.capitole.sisu.products.application.service;

import com.capitole.sisu.products.domain.price.CurrentPrice;
import com.capitole.sisu.products.domain.price.Price;
import com.capitole.sisu.products.port.in.rest.RestPriceInPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class PriceService implements RestPriceInPort {

    @Override
    public Mono<CurrentPrice> getCurrentPrice(Long productId, Long brandId, LocalDateTime date) {
        return null;
    }

    @Override
    public Mono<Page<Price>> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Mono<Price> create(Price price) {
        return null;
    }


    @Override
    public Mono<Void> delete(Long id) {
        return null;
    }
}
