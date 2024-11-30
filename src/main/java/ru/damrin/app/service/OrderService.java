package ru.damrin.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.OrdersGoodsRepository;
import ru.damrin.app.mapper.OrderMapper;
import ru.damrin.app.model.order.CreateOrderDto;
import ru.damrin.app.model.order.OrderDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrdersGoodsRepository ordersGoodsRepository;
  private final OrderMapper orderMapper;
  private final GoodRepository goodRepository;

//  public List<OrderDto> findAll() {
//    return ordersGoodsRepository.findAll().stream()
//        .map(orderMapper::toDto)
//        .toList();
//  }
//
//  public List<OrderDto> findAllByUserId(long userId) {
//    return repository.findAllByUserIdAndCreatedAt(userId, LocalDate.now()).stream()
//        .map(orderMapper::toDto)
//        .toList();
//  }

  public void createOrder(CreateOrderDto orderDto) {
    var invalidQuantityGoods = orderDto.goodOrders().stream()
        .map(goodOrderDto -> {
          var good = goodRepository.findById(goodOrderDto.goodId())
              .orElseThrow(() -> new WarehouseAppException(
                  String.format("Товар с идентификатором %s не найден", goodOrderDto.goodId())));
          if (good.getBalance().compareTo(goodOrderDto.quantity()) < 0) {
            return good;
          }
          return null;
        })
        .filter(Objects::nonNull)
        .map(good -> ((GoodEntity) good).getName())
        .collect(Collectors.joining(", "));

    if (StringUtils.isNotEmpty(invalidQuantityGoods)) {
      throw new WarehouseAppException(
          String.format("Нет необходимого количества товаров: %s на складе", invalidQuantityGoods),
          "Проверьте количество и повторите");
    }
  }
}
