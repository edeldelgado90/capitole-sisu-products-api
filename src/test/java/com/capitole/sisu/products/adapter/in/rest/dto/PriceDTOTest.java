package com.capitole.sisu.products.adapter.in.rest.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceDTOTest {

    private final Validator validator;

    public PriceDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testPriceDTOCreation() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        PriceDTO priceDTO =
                PriceDTO.builder()
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

        assertThat(priceDTO).isNotNull();
        assertThat(priceDTO.getId()).isEqualTo(1L);
        assertThat(priceDTO.getBrandId()).isEqualTo(1L);
        assertThat(priceDTO.getStartDate()).isEqualTo(startDate);
        assertThat(priceDTO.getEndDate()).isEqualTo(endDate);
        assertThat(priceDTO.getPriceList()).isEqualTo(1L);
        assertThat(priceDTO.getProductId()).isEqualTo(35455L);
        assertThat(priceDTO.getPriority()).isEqualTo(1);
        assertThat(priceDTO.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(priceDTO.getCurr()).isEqualTo("EUR");
    }

    @Test
    public void testBrandIdNotNull() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(null)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.brandId.notNull}");
    }

    @Test
    public void testStartDateCannotBeNull() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(null)
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.startDate.notNull}");
    }

    @Test
    public void testEndDateCannotBeNull() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(null)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.endDate.notNull}");
    }

    @Test
    public void testCurrCannotBeBlank() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.curr.notBlank}");
    }

    @Test
    public void testPriceListCannotBeNegative() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(-1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.priceList.PositiveOrZero}");
    }

    @Test
    public void testProductIdCannotBeNegative() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(-35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.productId.PositiveOrZero}");
    }

    @Test
    public void testPriorityCannotBeNegative() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(-1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.priority.PositiveOrZero}");
    }

    @Test
    public void testPriceCannotBeNegative() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(-100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.price.PositiveOrZero}");
    }

    @Test
    public void testStartDateBeforeEndDate() {
        LocalDateTime startDate = LocalDateTime.of(2023, 12, 31, 23, 59);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 0, 0);

        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.startDate.beforeEndDate}");
    }

    @Test
    public void testStartDateBeforeEndDateValid() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).isEmpty();
    }

    @Test
    public void testStartDateBeforeEndDateWithEqualDates() {
        LocalDateTime startDate = LocalDateTime.of(2023, 12, 31, 23, 59);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.startDate.beforeEndDate}");
    }

    @Test
    public void testPriceWithValidValues() {
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

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).isEmpty();
    }

    @Test
    public void testPriceWithZeroValue() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.ZERO)
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).isEmpty();
    }

    @Test
    public void testPriceWithNegativeValue() {
        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(-100.00))
                        .curr("EUR")
                        .build();

        Set<ConstraintViolation<PriceDTO>> validate = validator.validate(priceDTO);
        assertThat(validate).hasSize(1);
        assertThat(validate.iterator().next().getMessage()).isEqualTo("{price.price.PositiveOrZero}");
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        PriceDTO priceDTO1 =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        PriceDTO priceDTO2 =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        assertThat(priceDTO1).isEqualTo(priceDTO2);

        assertThat(priceDTO1.hashCode()).isEqualTo(priceDTO2.hashCode());
    }

    @Test
    public void testToString() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        PriceDTO priceDTO =
                PriceDTO.builder()
                        .brandId(1L)
                        .startDate(startDate)
                        .endDate(endDate)
                        .priceList(1L)
                        .productId(35455L)
                        .priority(1)
                        .price(BigDecimal.valueOf(100.00))
                        .curr("EUR")
                        .build();

        String expectedToString = "PriceDTO(brandId=1, startDate=2023-01-01T00:00, endDate=2023-12-31T23:59, priceList=1, productId=35455, priority=1, price=100.0, curr=EUR, id=null)";
        assertThat(priceDTO.toString()).isEqualTo(expectedToString);
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 12, 31, 23, 59);

        PriceDTO priceDTO = new PriceDTO(1L, startDate, endDate, 1L, 35455L, 1, BigDecimal.valueOf(100.00), "EUR", 1L);

        assertThat(priceDTO.getBrandId()).isEqualTo(1L);
        assertThat(priceDTO.getStartDate()).isEqualTo(startDate);
        assertThat(priceDTO.getEndDate()).isEqualTo(endDate);
        assertThat(priceDTO.getPriceList()).isEqualTo(1L);
        assertThat(priceDTO.getProductId()).isEqualTo(35455L);
        assertThat(priceDTO.getPriority()).isEqualTo(1);
        assertThat(priceDTO.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(priceDTO.getCurr()).isEqualTo("EUR");
        assertThat(priceDTO.getId()).isEqualTo(1L);
    }
}
