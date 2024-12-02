package ru.damrin.app.model;

/**
 * DTO for {@link ru.damrin.app.db.entity.OrdersGoodsEntity}
 */
public record OrdersGoodsDto(Long id, String goodName, Long sum, Long quantity) {
  public OrdersGoodsDto(Long id, String goodName, Long quantity) {
    this(id, goodName, null, quantity);
  }
}