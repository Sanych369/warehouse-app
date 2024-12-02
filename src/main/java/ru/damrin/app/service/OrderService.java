package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.entity.OrderEntity;
import ru.damrin.app.db.entity.OrdersGoodsEntity;
import ru.damrin.app.db.repository.CompanyRepository;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.OrdersRepository;
import ru.damrin.app.db.repository.UserRepository;
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

  private final OrderMapper orderMapper;
  private final GoodRepository goodRepository;
  private final UserRepository userRepository;
  private final OrdersRepository ordersRepository;
  private final CompanyRepository companyRepository;

  public List<OrderDto> findAll() {
    return ordersRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(orderMapper::toDto)
        .toList();
  }

  public List<OrderDto> findAllByUserId(Long userId) {
    return ordersRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(orderMapper::toDto)
        .toList();
  }

  public List<OrderDto> findCurrentOrdersByUserId(long userId) {
    return ordersRepository.findAllByUserIdAndCreatedAt(userId, LocalDate.now()).stream()
        .map(orderMapper::toDto)
        .toList();
  }

  public void createOrder(CreateOrderDto orderDto) {
    var company = companyRepository.findById(orderDto.companyId())
        .orElseThrow(() -> new WarehouseAppException("Компания не найдена."));
    if (!company.getIsActive()) {
      throw new WarehouseAppException("Компания неактивна. Заказы оформляются только на активные компании");
    }
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
          String.format("Нет необходимого количества товаров: %s на складе. Проверьте количество и повторите", invalidQuantityGoods));
    }

    //TODO: берётся из сесурьки
    long userId = 1L;
    var user = userRepository.findById(userId).orElse(null);

    var order = OrderEntity.builder()
        .user(user)
        .company(companyRepository.getReferenceById(orderDto.companyId()))
        .build();
    orderDto.goodOrders().forEach(goodOrderDto -> {

      final var good = goodRepository.findById(goodOrderDto.goodId())
          .orElseThrow(() -> new WarehouseAppException("Товар не найден"));

      order.addOrdersGoods(
          OrdersGoodsEntity.builder()
              .sum(good.getSalePrice() * goodOrderDto.quantity())
              .goodName(good.getName())
              .quantity(goodOrderDto.quantity())
              .build());
    });

    ordersRepository.save(order);
  }
}
