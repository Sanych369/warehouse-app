package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.damrin.app.model.OrdersGoodsDto;
import ru.damrin.app.model.order.AddGoodToOrderDto;
import ru.damrin.app.model.order.CreateOrderDto;
import ru.damrin.app.model.order.OrderDto;
import ru.damrin.app.service.OrderService;
import ru.damrin.app.service.OrdersGoodsService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final OrderService service;
  private final OrdersGoodsService ordersGoodsService;

  @GetMapping("/my")
  public ResponseEntity<List<OrderDto>> getMyOrder() {
//  TODO: тут возврат по юзеру из контекста сесурити
    long userId = 1L;
    return ResponseEntity.ok(service.findAllByUserId(userId));
  }

  @GetMapping("/all")
  public ResponseEntity<List<OrderDto>> getAllOrders() {
    return ResponseEntity.ok(service.findAll());
  }

  @PostMapping("/create")
  public ResponseEntity<Void> createOrder(@RequestBody CreateOrderDto createOrderDto) {
    service.createOrder(createOrderDto);
    return ResponseEntity.status(CREATED).build();
  }

  @PostMapping("/change")
  public ResponseEntity<Void> changeOrder(@RequestBody OrdersGoodsDto orderDto) {
    ordersGoodsService.changeOrdersGoodsByOrderId(orderDto);
    return ResponseEntity.status(OK).build();
  }

  @PostMapping("/add-good")
  public ResponseEntity<Void> addGoodsToOrder(@RequestBody AddGoodToOrderDto addGoodToOrderDto) {
    ordersGoodsService.addGoodToOrdersGoods(addGoodToOrderDto.orderId(), addGoodToOrderDto.goodName(), addGoodToOrderDto.quantity());
    return ResponseEntity.status(CREATED).build();
  }

  @GetMapping("/goods")
  public ResponseEntity<List<OrdersGoodsDto>> getOrdersGoods(@RequestParam Long orderId) {
    return ResponseEntity.ok(ordersGoodsService.getOrdersGoodsByOrderId(orderId));
  }

  @PostMapping("/delete-good")
  public ResponseEntity<Void> deleteGood(@RequestParam Long ordersGoodsId, @RequestParam Long orderId) {
    ordersGoodsService.deleteOrdersGoodsByOrderId(ordersGoodsId, orderId);
    return ResponseEntity.status(OK).build();
  }
}
