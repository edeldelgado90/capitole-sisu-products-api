package com.capitole.sisu.products.domain.price;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CurrentPrice {
    private Long productId;
    private Long brandId;
    private Long priceList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal price;
}
