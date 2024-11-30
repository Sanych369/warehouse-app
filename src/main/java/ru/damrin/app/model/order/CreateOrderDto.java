package ru.damrin.app.model.order;

import ru.damrin.app.model.good.GoodOrderDto;

import java.util.Set;

public record CreateOrderDto(Long companyId, Set<GoodOrderDto> goodOrders) {
}
