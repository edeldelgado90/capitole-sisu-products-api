package com.capitole.sisu.products.domain.price;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrentPriceTest {

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPrice price1 =
                CurrentPrice.builder()
                        .productId(35455L)
                        .brandId(1L)
                        .priceList(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .price(BigDecimal.valueOf(100.00))
                        .build();

        CurrentPrice price2 =
                CurrentPrice.builder()
                        .productId(35455L)
                        .brandId(1L)
                        .priceList(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .price(BigDecimal.valueOf(100.00))
                        .build();

        assertThat(price1).isEqualTo(price2);

        assertThat(price1.hashCode()).isEqualTo(price2.hashCode());
    }

    @Test
    public void testToString() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPrice price =
                CurrentPrice.builder()
                        .productId(35455L)
                        .brandId(1L)
                        .priceList(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .price(BigDecimal.valueOf(100.00))
                        .build();

        String expectedToString = "CurrentPrice(productId=35455, brandId=1, priceList=1, startDate=2023-01-01T00:00, endDate=2023-12-31T23:59, price=100.0)";
        assertThat(price.toString()).isEqualTo(expectedToString);
    }

    @Test
    public void testGettersAndSetters() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPrice price = CurrentPrice.builder()
                .productId(35455L)
                .brandId(1L)
                .priceList(1L)
                .startDate(startDate)
                .endDate(endDate)
                .price(BigDecimal.valueOf(100.00))
                .build();

        assertThat(price.getProductId()).isEqualTo(35455L);
        assertThat(price.getBrandId()).isEqualTo(1L);
        assertThat(price.getPriceList()).isEqualTo(1L);
        assertThat(price.getStartDate()).isEqualTo(startDate);
        assertThat(price.getEndDate()).isEqualTo(endDate);
        assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPrice price = new CurrentPrice(35455L, 1L, 1L, startDate, endDate, BigDecimal.valueOf(100.00));

        assertThat(price.getProductId()).isEqualTo(35455L);
        assertThat(price.getBrandId()).isEqualTo(1L);
        assertThat(price.getPriceList()).isEqualTo(1L);
        assertThat(price.getStartDate()).isEqualTo(startDate);
        assertThat(price.getEndDate()).isEqualTo(endDate);
        assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
    }
}