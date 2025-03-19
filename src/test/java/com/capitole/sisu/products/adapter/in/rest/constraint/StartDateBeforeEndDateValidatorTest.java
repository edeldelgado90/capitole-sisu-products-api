package com.capitole.sisu.products.adapter.in.rest.constraint;

import com.capitole.sisu.products.adapter.in.rest.dto.PriceDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class StartDateBeforeEndDateValidatorTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidPriceDTO() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        assertThat(validator.validate(priceDTO)).isEmpty();
    }

    @Test
    public void testStartDateAfterEndDate() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .endDate(LocalDateTime.of(2023, 1, 1, 0, 0)) // Inverso
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        assertThat(validator.validate(priceDTO)).isNotEmpty();
    }
}
