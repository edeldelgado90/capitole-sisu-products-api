package com.capitole.sisu.products.domain.price;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Price {
  private Long id;
  private final Long brandId;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final Long priceList;
  private final Long productId;
  private final Integer priority;
  private final BigDecimal price;
  private final String curr;
}
