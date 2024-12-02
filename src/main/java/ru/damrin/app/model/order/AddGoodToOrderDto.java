package ru.damrin.app.model.order;

public record AddGoodToOrderDto(Long orderId, String goodName, Long quantity) {
}
