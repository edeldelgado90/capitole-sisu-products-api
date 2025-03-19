package com.capitole.sisu.products.adapter.in.rest.controller;

import com.capitole.sisu.products.adapter.in.rest.dto.PriceDTO;
import com.capitole.sisu.products.adapter.in.rest.mapper.PriceDtoMapper;
import com.capitole.sisu.products.domain.price.CurrentPrice;
import com.capitole.sisu.products.domain.price.Price;
import com.capitole.sisu.products.port.in.rest.RestPricePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final RestPricePort restPricePort;
    private final PriceDtoMapper priceDtoMapper;

    public PriceController(RestPricePort restPricePort, PriceDtoMapper priceMapper) {
        this.restPricePort = restPricePort;
        this.priceDtoMapper = priceMapper;
    }

    @PostMapping
    public Mono<Price> create(@Valid @RequestBody PriceDTO price) {
        return restPricePort.create(priceDtoMapper.fromPriceDTOToPrice(price));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return restPricePort.delete(id);
    }

    @GetMapping("/current")
    public Mono<CurrentPrice> getCurrentPrice(@RequestParam(name = "product_id") Long productId, @RequestParam(name = "brand_id") Long brandId, @RequestParam LocalDateTime date) {
        return restPricePort.getCurrentPrice(productId, brandId, date);
    }

    @GetMapping("/")
    public Mono<Page<Price>> findAll(@PageableDefault Pageable pageable) {
        return restPricePort.findAll(pageable);
    }
}
