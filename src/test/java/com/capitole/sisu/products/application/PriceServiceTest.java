package com.capitole.sisu.products.application;

import com.capitole.sisu.products.application.cache.CacheConstants;
import com.capitole.sisu.products.application.cache.CacheEvictionService;
import com.capitole.sisu.products.application.service.PriceService;
import com.capitole.sisu.products.domain.price.*;
import com.capitole.sisu.products.port.out.DatabasePriceInPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PriceServiceTest {

    private final Long productId = 35455L;
    private final Long brandId = 1L;
    private final LocalDateTime date = LocalDateTime.of(2023, 10, 1, 12, 0);

    @InjectMocks
    private PriceService priceService;
    @Mock
    private DatabasePriceInPort priceRepository;
    @Mock
    private PriceManager priceManager;
    @Mock
    private Cache cache;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private CacheEvictionService cacheEvictionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createPriceSuccessfully() {
        Price price =
                Price.builder()
                        .brandId(brandId)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .priceList(1L)
                        .productId(productId)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Flux<Price> prices = Flux.empty();
        when(priceRepository.save(price)).thenReturn(Mono.just(price));
        when(priceRepository.findAllByProductIdAndBrandId(price.getProductId(), price.getBrandId()))
                .thenReturn(prices);
        when(priceManager.doesPriceOverlap(prices, price)).thenReturn(Mono.just(false));

        Mono<Price> savedPrice = priceService.create(price);

        assertThat(savedPrice.block()).isEqualTo(price);
        verify(priceRepository).save(price);
    }

    @Test
    public void createPriceOverlappingMustReturnException() {
        Price price =
                Price.builder()
                        .brandId(brandId)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .priceList(1L)
                        .productId(productId)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Flux<Price> prices = Flux.just(price);
        when(priceRepository.findAllByProductIdAndBrandId(price.getProductId(), price.getBrandId()))
                .thenReturn(prices);
        when(priceManager.doesPriceOverlap(prices, price)).thenReturn(Mono.just(true));

        assertThatExceptionOfType(PriceOverlappingException.class)
                .isThrownBy(() -> priceService.create(price).block())
                .withMessage("Price overlaps with an existing price.");
    }

    @Test
    public void deletePriceSuccessfully() {
        Long priceId = 1L;
        Price price = Price.builder().id(priceId).build();

        when(priceRepository.findById(priceId)).thenReturn(Mono.just(price));
        when(priceRepository.delete(priceId)).thenReturn(Mono.empty());
        when(cacheEvictionService.evictCurrentPricesCache(price.getProductId(), price.getBrandId(), price.getStartDate()))
                .thenReturn(Mono.empty());

        Mono<Void> result = priceService.delete(priceId);

        assertThat(result.block()).isNull();
        verify(priceRepository).delete(priceId);
    }

    @Test
    public void deletePriceNotFoundShouldThrowException() {
        Long priceId = 999L;

        when(priceRepository.findById(priceId)).thenReturn(Mono.empty());

        assertThatExceptionOfType(PriceNotFoundException.class)
                .isThrownBy(() -> priceService.delete(priceId).block())
                .withMessage("Price with ID 999 not found.");
    }

    @Test
    public void findAllByPageable() {
        Pageable pageable = Pageable.ofSize(1).withPage(0);
        Price price = Price.builder().id(1L).brandId(brandId).build();
        List<Price> prices = List.of(price);
        Page<Price> pricePage = new PageImpl<>(prices, pageable, prices.size());

        when(priceRepository.findAll(pageable)).thenReturn(Mono.just(pricePage));

        Mono<Page<Price>> resultPage = priceService.findAll(pageable);

        assertThat(resultPage.block()).isEqualTo(pricePage);
        verify(priceRepository).findAll(pageable);
    }

    @Test
    public void getCurrentPriceNotFound() {
        long productId = 123L;
        long brandId = 1L;
        LocalDateTime date = LocalDateTime.now();

        when(priceRepository.getCurrentPriceByProductAndBrand(productId, brandId, date))
                .thenReturn(Mono.empty());
        when(cacheManager.getCache("currentPrices")).thenReturn(cache);

        assertThatExceptionOfType(PriceNotFoundException.class)
                .isThrownBy(
                        () -> priceService.getCurrentPrice(productId, brandId, date).block())
                .withMessage("No price found for the given product and brand.");
    }

    @Test
    public void testGetCurrentPrice_CacheNotFound() {
        when(cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE)).thenReturn(null);

        Mono<CurrentPrice> result = priceService.getCurrentPrice(productId, brandId, date);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof IllegalStateException && ex.getMessage().equals("Cache not found"))
                .verify();
    }

    @Test
    public void testGetCurrentPrice_CacheEmpty() {
        when(cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE)).thenReturn(cache);
        when(cache.get(productId + "-" + brandId, TreeMap.class)).thenReturn(null);

        CurrentPrice priceEntity = CurrentPrice.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(1L)
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .price(BigDecimal.valueOf(100.00))
                .build();
        when(priceRepository.getCurrentPriceByProductAndBrand(productId, brandId, date)).thenReturn(Mono.just(priceEntity));

        Mono<CurrentPrice> result = priceService.getCurrentPrice(productId, brandId, date);

        StepVerifier.create(result)
                .expectNextMatches(price ->
                        price.getProductId().equals(productId) &&
                                price.getBrandId().equals(brandId) &&
                                price.getPrice().equals(BigDecimal.valueOf(100.00))
                )
                .verifyComplete();

        verify(cache).put(eq(productId + "-" + brandId), any(TreeMap.class));
    }

    @Test
    public void testGetCurrentPrice_NoMatchInCache() {
        TreeMap<LocalDateTime, CurrentPrice> cachedPrices = new TreeMap<>();
        CurrentPrice currentPriceCached = CurrentPrice.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(1L)
                .startDate(date.minusDays(2))
                .endDate(date.minusDays(1))
                .price(BigDecimal.valueOf(90.00))
                .build();
        cachedPrices.put(date.minusDays(2), currentPriceCached);

        when(cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE)).thenReturn(cache);
        when(cache.get(productId + "-" + brandId, TreeMap.class)).thenReturn(cachedPrices);

        CurrentPrice currentPrice = CurrentPrice.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(1L)
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .price(BigDecimal.valueOf(100.00))
                .build();
        when(priceRepository.getCurrentPriceByProductAndBrand(productId, brandId, date)).thenReturn(Mono.just(currentPrice));

        Mono<CurrentPrice> result = priceService.getCurrentPrice(productId, brandId, date);

        StepVerifier.create(result)
                .expectNextMatches(price ->
                        price.getProductId().equals(productId) &&
                                price.getBrandId().equals(brandId) &&
                                price.getPrice().equals(BigDecimal.valueOf(100.00))
                )
                .verifyComplete();

        verify(cache).put(eq(productId + "-" + brandId), any(TreeMap.class));
    }

    @Test
    public void testGetCurrentPrice_MatchInCache() {
        TreeMap<LocalDateTime, CurrentPrice> cachedPrices = new TreeMap<>();
        CurrentPrice cachedPrice = CurrentPrice.builder()
                .productId(productId)
                .brandId(brandId)
                .priceList(1L)
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .price(BigDecimal.valueOf(100.00))
                .build();
        cachedPrices.put(date.minusDays(1), cachedPrice);

        when(cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE)).thenReturn(cache);
        when(cache.get(productId + "-" + brandId, TreeMap.class)).thenReturn(cachedPrices);

        Mono<CurrentPrice> result = priceService.getCurrentPrice(productId, brandId, date);

        StepVerifier.create(result)
                .expectNext(cachedPrice)
                .verifyComplete();

        verify(priceRepository, never()).getCurrentPriceByProductAndBrand(any(), any(), any());
    }

    @Test
    public void testGetCurrentPrice_PriceNotFoundInRepository() {
        when(cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE)).thenReturn(cache);
        when(cache.get(productId + "-" + brandId, TreeMap.class)).thenReturn(null);

        when(priceRepository.getCurrentPriceByProductAndBrand(productId, brandId, date)).thenReturn(Mono.empty());

        Mono<CurrentPrice> result = priceService.getCurrentPrice(productId, brandId, date);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof PriceNotFoundException
                        && ex.getMessage().equals("No price found for the given product and brand."))
                .verify();

        verify(cache, never()).put(any(), any());
    }
}
