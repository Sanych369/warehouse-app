package ru.damrin.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.entity.OrdersGoodsEntity;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.OrdersGoodsRepository;
import ru.damrin.app.db.repository.OrdersRepository;
import ru.damrin.app.mapper.OrdersGoodsMapper;
import ru.damrin.app.model.OrdersGoodsDto;
import ru.damrin.app.model.order.AddGoodToOrderDto;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersGoodsService {

  private final OrdersGoodsRepository ordersGoodsRepository;
  private final OrdersGoodsMapper ordersGoodsMapper;
  private final OrdersRepository ordersRepository;
  private final GoodRepository goodRepository;

  public List<OrdersGoodsDto> getOrdersGoodsByOrderId(Long orderId) {
    return ordersGoodsRepository.findAllByOrderId(orderId).stream()
        .map(ordersGoodsMapper::toDto)
        .toList();
  }

  @Transactional
  public void addGoodToOrdersGoods(List<AddGoodToOrderDto> goods) {
    goods.forEach(addGood -> {
      var good = goodRepository.findById(addGood.goodId())
          .orElseThrow(() -> new WarehouseAppException("Товар не найден"));

      var quantity = addGood.quantity();

      if (quantity > good.getBalance()) {
        throw new WarehouseAppException(
            String.format("Недостаточно товара на складе. Максимально доступно %s единиц для списания.", good.getBalance()));
      }

      var order = ordersRepository.findById(addGood.orderId())
          .orElseThrow(() -> new WarehouseAppException("Заказ не найден"));

      if (order.getCreatedAt().isBefore(LocalDate.now())) {
        throw new WarehouseAppException("Невозможно изменить ранее созданные заказы");
      }

      good.setBalance(good.getBalance() - quantity);

      order.addOrdersGoods(OrdersGoodsEntity.builder()
          .good(good)
          .quantity(quantity)
          .sum(good.getSalePrice() * quantity)
          .build());

      ordersRepository.save(order);
      log.info("Adding {} good {} to order {}", quantity, good.getName(), addGood.orderId());
    });
  }

  public void changeOrdersGoodsByOrderId(List<OrdersGoodsDto> ordersGoods) {
    ordersGoods.forEach(ordersGoodsDto -> {
      log.info("Changed order by good {} quantity {}", ordersGoodsDto.goodName(), ordersGoodsDto.quantity());

      var orderGood = ordersGoodsRepository.findById(ordersGoodsDto.id())
          .orElseThrow(() -> new WarehouseAppException("Данный товар не найден в заказе."));

      if (orderGood.getOrder().getCreatedAt().isBefore(LocalDate.now())) {
        throw new WarehouseAppException("Невозможно изменить ранее созданные заказы");
      }

      final var newQuantity = ordersGoodsDto.quantity();
      var good = checkGoodAndSetNewBalance(orderGood, newQuantity);

      orderGood.setGood(good);
      orderGood.setQuantity(newQuantity);
      orderGood.setSum(newQuantity * good.getSalePrice());
      orderGood.getOrder().addOrdersGoods(orderGood);

      ordersGoodsRepository.save(orderGood);
      log.info("Order was changed successfully");
    });
  }

  @Transactional
  public void deleteOrdersGoodsByOrderId(Long ordersGoodsId) {
    var ordersGood = ordersGoodsRepository.findById(ordersGoodsId)
        .orElseThrow(() -> new WarehouseAppException("Нет товаров в заказе"));
    var good = ordersGood.getGood();
    var goodName = ordersGood.getGoodName();
    var order = ordersGood.getOrder();

    if (order == null) {
      throw new WarehouseAppException("Заказ отсутствует для данной позиции. Обновите список заказов.");
    }

    if (order.getCreatedAt().isBefore(LocalDate.now())) {
      throw new WarehouseAppException("Невозможно изменить ранее созданные заказы");
    }

    log.info("Deleting good: {} from order with id: {}", goodName, order.getId());

    order.removeOrdersGoodsById(ordersGoodsId);
    good.setBalance(good.getBalance() + ordersGood.getQuantity());
    ordersRepository.save(order);
    log.info("Successfully deleted good: {} from order with id: {}", goodName, order.getId());
  }

  private static GoodEntity checkGoodAndSetNewBalance(OrdersGoodsEntity orderGood, Long newQuantity) {
    final var orderQuantity = orderGood.getQuantity();

    if (orderQuantity <= 0) {
      throw new WarehouseAppException("Новое количество не может быть меньше или равно нулю. Если нет необходимости в оформлении данной позиции - удалите");
    }

    if (newQuantity.equals(orderQuantity)) {
      throw new WarehouseAppException("Новые данные идентичны существующему заказу");
    }

    var good = orderGood.getGood();

    if (good == null) {
      throw new WarehouseAppException("Товар не найден");
    }

    final var quantityDifference = newQuantity - orderQuantity;

    final var currentBalance = good.getBalance();

    final var newBalance = currentBalance - quantityDifference;

    if (newBalance < 0) {
      throw new WarehouseAppException(String.format("Недостаточно товара на складе. Максимально доступно %s единиц для списания.", currentBalance));
    }

    good.setBalance(newBalance);
    return good;
  }
}
