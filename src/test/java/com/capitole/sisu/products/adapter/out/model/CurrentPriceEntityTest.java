package com.capitole.sisu.products.adapter.out.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrentPriceEntityTest {

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPriceEntity entity1 =
                CurrentPriceEntity.builder()
                        .productId(35455L)
                        .brandId(1L)
                        .priceList(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .price(BigDecimal.valueOf(100.00))
                        .build();

        CurrentPriceEntity entity2 =
                CurrentPriceEntity.builder()
                        .productId(35455L)
                        .brandId(1L)
                        .priceList(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .price(BigDecimal.valueOf(100.00))
                        .build();

        assertThat(entity1).isEqualTo(entity2);

        assertThat(entity1.hashCode()).isEqualTo(entity2.hashCode());
    }

    @Test
    public void testToString() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPriceEntity entity =
                CurrentPriceEntity.builder()
                        .productId(35455L)
                        .brandId(1L)
                        .priceList(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .price(BigDecimal.valueOf(100.00))
                        .build();

        String expectedToString = "CurrentPriceEntity(productId=35455, brandId=1, priceList=1, startDate=2023-01-01T00:00, endDate=2023-12-31T23:59, price=100.0)";
        assertThat(entity.toString()).isEqualTo(expectedToString);
    }

    @Test
    public void testGettersAndSetters() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPriceEntity entity = CurrentPriceEntity.builder()
                .productId(35455L)
                .brandId(1L)
                .priceList(1L)
                .startDate(startDate)
                .endDate(endDate)
                .price(BigDecimal.valueOf(100.00))
                .build();

        assertThat(entity.getProductId()).isEqualTo(35455L);
        assertThat(entity.getBrandId()).isEqualTo(1L);
        assertThat(entity.getPriceList()).isEqualTo(1L);
        assertThat(entity.getStartDate()).isEqualTo(startDate);
        assertThat(entity.getEndDate()).isEqualTo(endDate);
        assertThat(entity.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        CurrentPriceEntity entity = new CurrentPriceEntity(35455L, 1L, 1L, startDate, endDate, BigDecimal.valueOf(100.00));

        assertThat(entity.getProductId()).isEqualTo(35455L);
        assertThat(entity.getBrandId()).isEqualTo(1L);
        assertThat(entity.getPriceList()).isEqualTo(1L);
        assertThat(entity.getStartDate()).isEqualTo(startDate);
        assertThat(entity.getEndDate()).isEqualTo(endDate);
        assertThat(entity.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
    }
}