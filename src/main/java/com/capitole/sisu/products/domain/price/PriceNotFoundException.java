package com.capitole.sisu.products.domain.price;

public class PriceNotFoundException extends RuntimeException {
  public PriceNotFoundException(String message) {
    super(message);
  }
}
