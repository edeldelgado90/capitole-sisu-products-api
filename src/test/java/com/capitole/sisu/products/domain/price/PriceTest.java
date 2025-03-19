package com.capitole.sisu.products.domain.price;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceTest {

    @Test
    public void testCreatePrice() {
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2021, 12, 31, 23, 59);
        Price price =
                Price.builder()
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

        assertThat(price).isNotNull();
        assertThat(price.getId()).isEqualTo(1L);
        assertThat(price.getBrandId()).isEqualTo(1L);
        assertThat(price.getStartDate()).isEqualTo(startDate);
        assertThat(price.getEndDate()).isEqualTo(endDate);
        assertThat(price.getPriceList()).isEqualTo(1L);
        assertThat(price.getProductId()).isEqualTo(35455L);
        assertThat(price.getPriority()).isEqualTo(1);
        assertThat(price.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(price.getCurr()).isEqualTo("EUR");
    }

    @Test
    public void testPriceEquality() {
        Price price1 =
                Price.builder()
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

        Price price2 =
                Price.builder()
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

        assertThat(price1).isEqualTo(price2);
    }

    @Test
    public void testPriceToString() {
        Price price =
                Price.builder()
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

        assertThat(price.toString()).contains("Price(id=1");
    }
}
