package ru.damrin.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final SortService sortService;
  private final OrderMapper orderMapper;
  private final GoodRepository goodRepository;
  private final UserRepository userRepository;
  private final OrdersRepository ordersRepository;
  private final CompanyRepository companyRepository;

  public Page<OrderDto> getPageOrders(String user,
                                      String company,
                                      Long totalAmount,
                                      LocalDate dateFrom, LocalDate dateTo,
                                      int page,
                                      int size,
                                      String sort) {

    final var pageable = PageRequest.of(page, size, sortService.getSortOrderForOrders(sort));
    return ordersRepository.findAllWithFilters(user, company, totalAmount, dateFrom, dateTo, pageable)
        .map(orderMapper::toDto);
  }

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

  @Transactional
  public void createOrder(CreateOrderDto orderDto) {
    log.info("Create order: {}", orderDto.goodOrders());
    final var company = companyRepository.findById(orderDto.companyId())
        .orElseThrow(() -> new WarehouseAppException("Компания не найдена."));

    if (!company.getIsActive()) {
      throw new WarehouseAppException("Компания неактивна. Заказы оформляются только на активные компании");
    }

    final var requestedGoods = orderDto.goodOrders().stream()
        .map(orderGoodDto -> goodRepository.findById(orderGoodDto.goodId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());

    log.info("Start validating goods by quantity");

    final var invalidQuantityGoods = requestedGoods.stream()
        .filter(good -> good.getBalance() <= 0)
        .map(GoodEntity::getName)
        .collect(Collectors.joining(", "));

    if (StringUtils.isNotEmpty(invalidQuantityGoods)) {
      throw new WarehouseAppException(
          String.format("Товара: %s нет в наличии на складе. Замените данные позиции.", invalidQuantityGoods));
    }

    //TODO: берётся из сесурьки
    long userId = 1L;
    var user = userRepository.findById(userId).orElse(null);

    var order = OrderEntity.builder()
        .user(user)
        .company(companyRepository.getReferenceById(orderDto.companyId()))
        .build();

    orderDto.goodOrders().forEach(goodOrderDto -> {
      var good = requestedGoods.stream()
          .filter(requestGood -> requestGood.getId().equals(goodOrderDto.goodId()))
          .findFirst()
          .orElseThrow(() -> new WarehouseAppException(
              String.format("Товар с идентификатором %s не найден в базе. Невозможно создать заказ.", goodOrderDto.goodId())));
      var newBalance = good.getBalance() - goodOrderDto.quantity();
      if (newBalance < 0) {
        throw new WarehouseAppException(
            String.format("Нет необходимого количества %s на остатках. Проверьте наличие.", good.getName()));
      }

      good.setBalance(newBalance);

      order.addOrdersGoods(
          OrdersGoodsEntity.builder()
              .sum(good.getSalePrice() * goodOrderDto.quantity())
              .good(good)
              .quantity(goodOrderDto.quantity())
              .build());
    });

    ordersRepository.save(order);
    log.info("Order created: {}", order.getId());
  }
}
