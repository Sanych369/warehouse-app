package ru.damrin.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.OrdersGoodsEntity;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.OrdersGoodsRepository;
import ru.damrin.app.db.repository.OrdersRepository;
import ru.damrin.app.mapper.OrdersGoodsMapper;
import ru.damrin.app.model.OrdersGoodsDto;
import ru.damrin.app.model.order.AddGoodToOrderDto;

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

  //  OrdersGoodsDto
  public void changeOrdersGoodsByOrderId(OrdersGoodsDto ordersGoodsDto) {
    log.info("Changed order by good {} quantity {}", ordersGoodsDto.goodName(), ordersGoodsDto.quantity());

    var orderGood = ordersGoodsRepository.findById(ordersGoodsDto.id())
        .orElseThrow(() -> new WarehouseAppException("Данный товар не найден в заказе."));

    final var orderQuantity = orderGood.getQuantity();

    if (orderQuantity <= 0) {
      throw new WarehouseAppException("Новое количество не может быть меньше или равно нулю. Если нет необходимости в оформлении данной позиции - удалите");
    }

    if (ordersGoodsDto.quantity().equals(orderQuantity)) {
      throw new WarehouseAppException("Новые данные идентичны существующему заказу");
    }

    var good = goodRepository.findByName(orderGood.getGoodName())
        .orElseThrow(() -> new WarehouseAppException("Товар не найден"));

    final var quantityDifference = ordersGoodsDto.quantity() - orderQuantity;
    final var currentBalance = good.getBalance();

    final var newBalance = currentBalance - quantityDifference;

    if (newBalance < 0) {
      throw new WarehouseAppException(String.format("Недостаточно товара на складе. Максимально доступно %s единиц для списания.", currentBalance));
    }

    good.setBalance(newBalance);
    orderGood.setGood(good);

    orderGood.setQuantity(ordersGoodsDto.quantity());
    orderGood.setSum(ordersGoodsDto.quantity() * good.getSalePrice());
    orderGood.getOrder().addOrdersGoods(orderGood);

    ordersGoodsRepository.save(orderGood);
    log.info("Order was changed successfully");
  }

  public void deleteOrdersGoodsByOrderId(Long ordersGoodsId, Long orderId) {
    var ordersGood = ordersGoodsRepository.findById(ordersGoodsId)
        .orElseThrow(() -> new WarehouseAppException("Нет товаров в заказе"));

    var goodName = ordersGood.getGoodName();

    log.info("Deleting good: {} from order with id: {}", goodName, orderId);

    var order = ordersRepository.findById(orderId)
        .orElseThrow(() -> new WarehouseAppException("Заказ не найден"));
    order.removeOrdersGoodsByName(goodName);

    var good = goodRepository.findByName(goodName)
        .orElseThrow(() -> new WarehouseAppException("Товар не найден"));

    good.setBalance(good.getBalance() + ordersGood.getQuantity());
    ordersRepository.save(order);
    goodRepository.save(good);
    log.info("Successfully deleted good: {} from order with id: {}", goodName, orderId);
  }
}
