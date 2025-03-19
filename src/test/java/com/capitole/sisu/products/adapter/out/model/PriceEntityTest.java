package com.capitole.sisu.products.adapter.out.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceEntityTest {

    @Test
    public void testPriceEntityCreation() {
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 31, 23, 59);

        PriceEntity priceEntity = PriceEntity.builder()
                .id(1L)
                .brandId(1L)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(1L)
                .productId(35455L)
                .priority(1)
                .price(BigDecimal.valueOf(100.00))
                .curr("EUR")
                .build();

        assertThat(priceEntity).isNotNull();
        assertThat(priceEntity.getId()).isEqualTo(1L);
        assertThat(priceEntity.getBrandId()).isEqualTo(1L);
        assertThat(priceEntity.getStartDate()).isEqualTo(startDate);
        assertThat(priceEntity.getEndDate()).isEqualTo(endDate);
        assertThat(priceEntity.getPriceList()).isEqualTo(1L);
        assertThat(priceEntity.getProductId()).isEqualTo(35455L);
        assertThat(priceEntity.getPriority()).isEqualTo(1);
        assertThat(priceEntity.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(priceEntity.getCurr()).isEqualTo("EUR");
    }

    @Test
    public void testPriceEntityEquality() {
        PriceEntity priceEntity1 = PriceEntity.builder()
                .id(1L)
                .brandId(1L)
                .startDate(LocalDateTime.of(2021, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2021, 12, 31, 23, 59))
                .priceList(1L)
                .productId(35455L)
                .priority(1)
                .price(BigDecimal.valueOf(100.00))
                .curr("EUR")
                .build();

        PriceEntity priceEntity2 = PriceEntity.builder()
                .id(1L)
                .brandId(1L)
                .startDate(LocalDateTime.of(2021, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2021, 12, 31, 23, 59))
                .priceList(1L)
                .productId(35455L)
                .priority(1)
                .price(BigDecimal.valueOf(100.00))
                .curr("EUR")
                .build();

        assertThat(priceEntity1).isEqualTo(priceEntity2);
    }

    @Test
    public void testPriceEntityToString() {
        PriceEntity priceEntity = PriceEntity.builder()
                .id(1L)
                .brandId(1L)
                .startDate(LocalDateTime.of(2021, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2021, 12, 31, 23, 59))
                .priceList(1L)
                .productId(35455L)
                .priority(1)
                .price(BigDecimal.valueOf(100.00))
                .curr("EUR")
                .build();

        assertThat(priceEntity.toString()).contains("PriceEntity(id=1");
    }
}
