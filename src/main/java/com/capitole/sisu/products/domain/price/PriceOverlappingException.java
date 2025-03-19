package com.capitole.sisu.products.domain.price;

public class PriceOverlappingException extends RuntimeException {
  public PriceOverlappingException(String message) {
    super(message);
  }
}
