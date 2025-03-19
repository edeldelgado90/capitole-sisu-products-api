package com.capitole.sisu.products.adapter.in.rest.controller;

import com.capitole.sisu.products.adapter.in.rest.dto.PriceDTO;
import com.capitole.sisu.products.application.cache.CacheConstants;
import com.capitole.sisu.products.domain.error.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class PriceControllerIT {

    private final Long productId = 35455L;
    private final Long brandId = 1L;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void clearCache() {
        if (cacheManager == null) {
            return;
        }

        Cache cache = cacheManager.getCache(CacheConstants.CURRENT_PRICES_CACHE);
        if (cache == null) {
            return;
        }

        cache.clear();
    }

    @Test
    @DisplayName("Test 1: Request at 10:00 on the 14th for product 35455 for brand 1 (ZARA)")
    public void getCurrentPriceAt202006141000ForProduct35455AndZaraSuccess() {
        webTestClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/api/prices/current")
                                        .queryParam("product_id", productId)
                                        .queryParam("brand_id", brandId)
                                        .queryParam("date", "2020-06-14T10:00:00")
                                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PriceDTO.class)
                .value(
                        price -> assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(35.50)));
    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on the 14th for product 35455 for brand 1 (ZARA)")
    public void getCurrentPriceAt202006141600ForProduct35455AndZaraSuccess() {
        webTestClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/api/prices/current")
                                        .queryParam("product_id", productId)
                                        .queryParam("brand_id", brandId)
                                        .queryParam("date", "2020-06-14T16:00:00")
                                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PriceDTO.class)
                .value(
                        price -> assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(25.45)));
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on the 14th for product 35455 for brand 1 (ZARA)")
    public void getCurrentPriceAt_202006142100ForProduct35455AndZaraSuccess() {
        webTestClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/api/prices/current")
                                        .queryParam("product_id", productId)
                                        .queryParam("brand_id", brandId)
                                        .queryParam("date", "2020-06-14T21:00:00")
                                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PriceDTO.class)
                .value(
                        price -> assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(35.50)));
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on the 15th for product 35455 for brand 1 (ZARA)")
    public void getCurrentPriceAt202006151000ForProduct35455AndZaraSuccess() {
        webTestClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/api/prices/current")
                                        .queryParam("product_id", productId)
                                        .queryParam("brand_id", brandId)
                                        .queryParam("date", "2020-06-15T10:00:00")
                                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PriceDTO.class)
                .value(
                        price -> assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(30.50)));
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on the 16th for product 35455 for brand 1 (ZARA)")
    public void getCurrentPriceAt202006162100ForProduct35455AndZaraSuccess() {
        webTestClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/api/prices/current")
                                        .queryParam("product_id", productId)
                                        .queryParam("brand_id", brandId)
                                        .queryParam("date", "2020-06-16T21:00:00")
                                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PriceDTO.class)
                .value(
                        price -> assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(38.95)));
    }

    @Test
    @DisplayName("Create price overlapping must return error")
    public void createPriceOverlappingMustReturnError() {
        PriceDTO price =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2020, 6, 15, 0, 0))
                        .endDate(LocalDateTime.of(2020, 7, 15, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(2)
                        .price(BigDecimal.valueOf(150.50))
                        .curr("EUR")
                        .build();

        webTestClient
                .post()
                .uri("/api/prices")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(price)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
                .expectBody(ErrorResponse.class)
                .value(
                        error -> {
                            assertThat(error.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
                            assertThat(error.getError()).isEqualTo("Price overlaps with an existing price.");
                        });
    }

    @Test
    @DisplayName("Create price successfully must return created price")
    public void createPriceSuccessfullyMustReturnCreatedPrice() {
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 31, 23, 59);
        PriceDTO price =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(2)
                        .price(BigDecimal.valueOf(200))
                        .curr("EUR")
                        .build();

        webTestClient
                .post()
                .uri("/api/prices")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(price)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PriceDTO.class)
                .value(
                        createdPrice -> {
                            assertThat(createdPrice.getBrandId()).isEqualTo(1L);
                            assertThat(createdPrice.getStartDate()).isEqualTo(startDate);
                            assertThat(createdPrice.getEndDate()).isEqualTo(endDate);
                            assertThat(createdPrice.getPriceList()).isEqualTo(1L);
                            assertThat(createdPrice.getProductId()).isEqualTo(35455L);
                            assertThat(createdPrice.getPriority()).isEqualTo(2);
                            assertThat(createdPrice.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(200));
                            assertThat(createdPrice.getCurr()).isEqualTo("EUR");
                        });
    }

    @Test
    @DisplayName("Create price with invalid data must return bad request")
    public void createPriceWithEmptyBrandIdMustReturnError() {
        PriceDTO priceDTO = PriceDTO.builder()
                .brandId(null)
                .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .priceList(1L)
                .productId(35455L)
                .priority(1)
                .price(BigDecimal.valueOf(100.00))
                .curr("EUR")
                .build();

        webTestClient.post()
                .uri("/api/prices")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(priceDTO)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @DisplayName("Delete non existed price must return error")
    public void deleteNonExistedPriceMustReturnError() {
        Long priceId = 100L;
        webTestClient
                .delete()
                .uri("/api/prices/{id}", priceId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .value(
                        error -> {
                            assertThat(error.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
                            assertThat(error.getError())
                                    .isEqualTo(String.format("Price with ID %d not found.", priceId));
                        });
    }

    @Test
    @DisplayName("Delete existed price must return ok")
    public void deleteExistedPriceMustReturnOk() {
        Long priceId = 1L;
        webTestClient
                .delete()
                .uri("/api/prices/{id}", priceId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @DisplayName("Request for all prices with pagination")
    public void getAllPricesWithPaginationSuccess() {
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/prices/")
                                .queryParam("page", 0)
                                .queryParam("size", 2)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PriceDTO.class)
                .value(prices -> {
                    assertThat(prices).isNotEmpty();
                    assertThat(prices.size()).isEqualTo(1);
                });
    }

    @Test
    @DisplayName("Request for all prices with multiple pages")
    public void getAllPricesWithMultiplePages() {
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/prices/")
                                .queryParam("page", 1)
                                .queryParam("size", 2)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PriceDTO.class)
                .value(prices -> {
                    assertThat(prices).isNotEmpty();
                    assertThat(prices.size()).isEqualTo(1);
                });
    }

    @Test
    @DisplayName("Request for all prices with page size greater than available prices")
    public void getAllPricesWithLargePageSize() {
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/prices/")
                                .queryParam("page", 0)
                                .queryParam("size", 100)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PriceDTO.class)
                .value(prices -> {
                    assertThat(prices).isNotEmpty();
                    assertThat(prices.size()).isEqualTo(1);
                });
    }

    @Test
    @DisplayName("Request for all prices with default pagination")
    public void getAllPricesWithDefaultPagination() {
        webTestClient
                .get()
                .uri("/api/prices/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PriceDTO.class)
                .value(prices -> {
                    assertThat(prices).isNotEmpty();
                    assertThat(prices.size()).isEqualTo(1);
                });
    }

}
