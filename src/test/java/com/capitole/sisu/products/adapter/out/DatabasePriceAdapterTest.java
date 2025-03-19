package com.capitole.sisu.products.adapter.out;

import com.capitole.sisu.products.adapter.out.model.PriceEntity;
import com.capitole.sisu.products.adapter.out.repository.PriceRepository;
import com.capitole.sisu.products.domain.price.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DatabasePriceAdapterTest {

    @InjectMocks
    private DatabasePriceAdapter databasePriceAdapter;

    @Mock
    private PriceRepository repository;

    @Mock
    private PriceMapper mapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePriceSuccessfully() {
        Price price =
                Price.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        PriceEntity priceEntity =
                PriceEntity.builder()
                        .id(1L)
                        .brandId(1L)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        when(mapper.fromPriceToPriceEntity(price)).thenReturn(priceEntity);
        when(repository.save(priceEntity)).thenReturn(Mono.just(priceEntity));
        when(mapper.fromPriceEntityToPrice(priceEntity)).thenReturn(price);

        Mono<Price> result = databasePriceAdapter.save(price);

        assertThat(result.block()).isEqualTo(price);
    }

    @Test
    public void testDeleteSuccessfully() {
        Long priceId = 1L;

        when(repository.deleteById(priceId)).thenReturn(Mono.empty());

        Mono<Void> result = databasePriceAdapter.delete(priceId);

        assertThat(result).isEqualTo(Mono.empty());
    }

    @Test
    public void testFindByIdSuccessfully() {
        Long priceId = 1L;
        PriceEntity priceEntity =
                PriceEntity.builder()
                        .id(priceId)
                        .brandId(1L)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        when(repository.findById(priceId)).thenReturn(Mono.just(priceEntity));
        when(mapper.fromPriceEntityToPrice(priceEntity)).thenReturn(Price.builder().build());

        Mono<Price> result = databasePriceAdapter.findById(priceId);

        assertThat(result.block()).isNotNull();
    }

    @Test
    public void testFindAllByPageable() {
        Pageable pageable = Pageable.ofSize(1).withPage(0);

        PriceEntity priceEntity =
                PriceEntity.builder()
                        .id(1L)
                        .brandId(1L)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        when(repository.findAllBy(pageable)).thenReturn(Flux.just(priceEntity));
        when(mapper.fromPriceEntityToPrice(priceEntity)).thenReturn(Price.builder().id(priceEntity.getId()).build());
        when(repository.count()).thenReturn(Mono.just(1L));

        Mono<Page<Price>> result = databasePriceAdapter.findAll(pageable);

        assertThat(result.block()).isNotNull();
        assertThat(result.block().getTotalElements()).isEqualTo(1);
    }

    @Test
    public void testFindAllByProductIdAndBrandId() {
        Long productId = 35455L;
        Long brandId = 1L;

        PriceEntity priceEntity1 =
                PriceEntity.builder()
                        .id(1L)
                        .brandId(brandId)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .priceList(1L)
                        .productId(productId)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        PriceEntity priceEntity2 =
                PriceEntity.builder()
                        .id(2L)
                        .brandId(brandId)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(2))
                        .priceList(1L)
                        .productId(productId)
                        .priority(2)
                        .price(BigDecimal.valueOf(150.00))
                        .curr("EUR")
                        .build();

        when(repository.findAllByProductIdAndBrandId(productId, brandId))
                .thenReturn(Flux.just(priceEntity1, priceEntity2));

        when(mapper.fromPriceEntityToPrice(any())).thenReturn(Price.builder().build());

        Flux<Price> results = databasePriceAdapter.findAllByProductIdAndBrandId(productId, brandId);

        assertThat(results.collectList().block()).hasSize(2);
    }
}
