package ru.damrin.app.model.order;

public record AddGoodToOrderDto(Long orderId, Long goodId, Long quantity) {
}
