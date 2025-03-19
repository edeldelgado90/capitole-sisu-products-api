package com.capitole.sisu.products.port.in.rest;

import reactor.core.publisher.Mono;

public interface RestPort<T> {

    Mono<T> create(T price);

    Mono<Void> delete(Long id);
}
