package ru.damrin.app.service;

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

  public void addGoodToOrdersGoods(Long orderId, String goodName, Long quantity) {
    var good = goodRepository.findByName(goodName)
        .orElseThrow(() -> new WarehouseAppException("Товар не найден"));
    if (quantity > good.getBalance()) {
      throw new WarehouseAppException(String.format("Недостаточно товара на складе. Максимально доступно %s единиц для списания.", good.getBalance()));
    }

    var order = ordersRepository.findById(orderId)
        .orElseThrow(() -> new WarehouseAppException("Заказ не найден"));

    order.addOrdersGoods(OrdersGoodsEntity.builder()
        .quantity(quantity)
        .goodName(goodName)
        .sum(good.getSalePrice() * quantity)
        .build());

    good.setBalance(good.getBalance() - quantity);

    ordersRepository.save(order);
    goodRepository.save(good);
  }

  //  OrdersGoodsDto
  public void changeOrdersGoodsByOrderId(OrdersGoodsDto ordersGoodsDto) {
    var orderGood = ordersGoodsRepository.findById(ordersGoodsDto.id())
        .orElseThrow(() -> new WarehouseAppException("Данный товар не найден в заказе."));
    if (orderGood.getQuantity() <= 0) {
      throw new WarehouseAppException("Новое количество не может быть меньше или равно нулю. Если нет необходимости в оформлении данной позиции - удалите");
    }

    if (ordersGoodsDto.quantity().equals(orderGood.getQuantity())) {
      throw new WarehouseAppException("Новые данные идентичны текущему заказу");
    }

    var good = goodRepository.findByName(orderGood.getGoodName())
        .orElseThrow(() -> new WarehouseAppException("Товар не найден"));

    var quantityDifference = ordersGoodsDto.quantity() - orderGood.getQuantity();
    var currentBalance = good.getBalance();

    var newBalance = currentBalance - quantityDifference;

    if (newBalance < 0) {
      throw new WarehouseAppException(String.format("Недостаточно товара на складе. Максимально доступно %s единиц для списания.", currentBalance));
    }

    good.setBalance(newBalance);

    orderGood.setQuantity(ordersGoodsDto.quantity());
    orderGood.setSum(ordersGoodsDto.quantity() * good.getSalePrice());
    orderGood.getOrder().addOrdersGoods(orderGood);

    goodRepository.save(good);
    ordersGoodsRepository.save(orderGood);
  }

  public void deleteOrdersGoodsByOrderId(Long ordersGoodsId, Long orderId) {
    var ordersGood = ordersGoodsRepository.findById(ordersGoodsId)
        .orElseThrow(() -> new WarehouseAppException("Нет товаров в заказе"));

    var goodName = ordersGood.getGoodName();

    var order = ordersRepository.findById(orderId)
        .orElseThrow(() -> new WarehouseAppException("Заказ не найден"));
    order.removeOrdersGoodsByName(goodName);

    var good = goodRepository.findByName(goodName)
        .orElseThrow(() -> new WarehouseAppException("Товар не найден"));

    //Возвращаем товары на баланс при удалении из заказа
    good.setBalance(good.getBalance() + ordersGood.getQuantity());
    ordersRepository.save(order);
    goodRepository.save(good);
  }
}
