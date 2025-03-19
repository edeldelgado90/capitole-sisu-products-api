package com.capitole.sisu.products.adapter.in.rest.dto;

import com.capitole.sisu.products.adapter.in.rest.constraint.StartDateBeforeEndDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@StartDateBeforeEndDate
public class PriceDTO {
    @NotNull(message = "{price.brandId.notNull}")
    @PositiveOrZero(message = "{price.brandId.PositiveOrZero}")
    private final Long brandId;
    @NotNull(message = "{price.startDate.notNull}")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime startDate;
    @NotNull(message = "{price.endDate.notNull}")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime endDate;
    @PositiveOrZero(message = "{price.priceList.PositiveOrZero}")
    private final Long priceList;
    @PositiveOrZero(message = "{price.productId.PositiveOrZero}")
    private final Long productId;
    @PositiveOrZero(message = "{price.priority.PositiveOrZero}")
    private final Integer priority;
    @PositiveOrZero(message = "{price.price.PositiveOrZero}")
    private final BigDecimal price;
    @NotBlank(message = "{price.curr.notBlank}")
    private final String curr;
    private Long id;
}
