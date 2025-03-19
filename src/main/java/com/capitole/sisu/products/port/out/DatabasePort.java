package com.capitole.sisu.products.port.out;

import com.capitole.sisu.products.domain.price.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface DatabasePort<T> {

    Mono<T> save(T price);

    Mono<Void> delete(Long id);

    Mono<Price> findById(Long id);

    Mono<Page<Price>> findAll(Pageable pageable);
}
