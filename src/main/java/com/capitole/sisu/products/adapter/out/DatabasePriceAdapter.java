package com.capitole.sisu.products.adapter.out;

import com.capitole.sisu.products.adapter.out.model.PriceEntity;
import com.capitole.sisu.products.adapter.out.repository.CurrentPriceRepository;
import com.capitole.sisu.products.adapter.out.repository.PriceRepository;
import com.capitole.sisu.products.domain.price.CurrentPrice;
import com.capitole.sisu.products.domain.price.Price;
import com.capitole.sisu.products.port.out.DatabasePriceInPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class DatabasePriceAdapter implements DatabasePriceInPort {

    private final PriceRepository priceRepository;
    private final CurrentPriceRepository currentPriceRepository;
    private final PriceMapper mapper;

    public DatabasePriceAdapter(PriceRepository priceRepository, CurrentPriceRepository currentPriceRepository, PriceMapper mapper) {
        this.priceRepository = priceRepository;
        this.currentPriceRepository = currentPriceRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Price> save(Price price) {
        PriceEntity priceEntity = mapper.fromPriceToPriceEntity(price);
        Mono<PriceEntity> response = priceRepository.save(priceEntity);
        return response.map(mapper::fromPriceEntityToPrice);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return priceRepository.deleteById(id);
    }

    @Override
    public Mono<Price> findById(Long id) {
        return priceRepository.findById(id).map(mapper::fromPriceEntityToPrice);
    }

    @Override
    public Mono<Page<Price>> findAll(Pageable pageable) {
        return this.priceRepository
                .findAllBy(pageable)
                .map(mapper::fromPriceEntityToPrice)
                .collectList()
                .flatMap(
                        prices ->
                                this.priceRepository
                                        .count()
                                        .defaultIfEmpty(0L)
                                        .map(count -> new PageImpl<>(prices, pageable, count)));
    }

    @Override
    public Mono<CurrentPrice> getCurrentPriceByProductAndBrand(
            Long productId, Long brandId, LocalDateTime date) {
        return currentPriceRepository.findByProductIdAndBrandIdAndDate(productId, brandId, date)
                .map(mapper::fromCurrentPriceEntityToCurrentPrice);
    }

    @Override
    public Flux<Price> findAllByProductIdAndBrandId(Long productId, Long brandId) {
        return priceRepository.findAllByProductIdAndBrandId(productId, brandId).map(mapper::fromPriceEntityToPrice);
    }
}
