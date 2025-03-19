package com.capitole.sisu.products.adapter.out.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Table(name = "prices")
public class PriceEntity {

  @Id private Long id;
  private Long brandId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Long priceList;
  private Long productId;
  private Integer priority;
  private BigDecimal price;
  private String curr;
}
