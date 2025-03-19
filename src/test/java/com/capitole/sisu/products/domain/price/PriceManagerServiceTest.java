package com.capitole.sisu.products.domain.price;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class PriceManagerServiceTest {

    @InjectMocks
    private PriceManagerService priceManagerService;

    private Price existingPrice1;
    private Price existingPrice2;
    private Price newPrice;

    @BeforeEach
    void setUp() {
        existingPrice1 = Price.builder()
                .startDate(LocalDateTime.of(2023, 10, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 10, 10, 0, 0))
                .build();

        existingPrice2 = Price.builder()
                .startDate(LocalDateTime.of(2023, 10, 15, 0, 0))
                .endDate(LocalDateTime.of(2023, 10, 20, 0, 0))
                .build();

        newPrice = Price.builder()
                .startDate(LocalDateTime.of(2023, 10, 5, 0, 0))
                .endDate(LocalDateTime.of(2023, 10, 12, 0, 0))
                .build();
    }

    @Test
    void doesPriceOverlap_WhenPricesOverlap_ReturnsTrue() {
        // Arrange
        Flux<Price> existingPrices = Flux.just(existingPrice1, existingPrice2);

        // Act
        Mono<Boolean> result = priceManagerService.doesPriceOverlap(existingPrices, newPrice);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void doesPriceOverlap_WhenPricesDoNotOverlap_ReturnsFalse() {
        // Arrange
        newPrice = Price.builder()
                .startDate(LocalDateTime.of(2023, 10, 11, 0, 0))
                .endDate(LocalDateTime.of(2023, 10, 14, 0, 0))
                .build();

        Flux<Price> existingPrices = Flux.just(existingPrice1, existingPrice2);

        // Act
        Mono<Boolean> result = priceManagerService.doesPriceOverlap(existingPrices, newPrice);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void doesPriceOverlap_WhenNoExistingPrices_ReturnsFalse() {
        // Arrange
        Flux<Price> existingPrices = Flux.empty();

        // Act
        Mono<Boolean> result = priceManagerService.doesPriceOverlap(existingPrices, newPrice);

        // Assert
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}