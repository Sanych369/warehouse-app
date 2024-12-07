package ru.damrin.app.model;

/**
 * DTO получения информации о заказе.
 */
public record OrdersGoodsDto(Long id, String goodName, Long sum, Long quantity) {
}