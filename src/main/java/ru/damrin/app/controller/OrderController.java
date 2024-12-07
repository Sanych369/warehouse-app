package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Контроллер для управлением заказами.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final OrderService service;
  private final OrdersGoodsService ordersGoodsService;

  @GetMapping("/page")
  public ResponseEntity<Page<OrderDto>> getOrdersPage(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "user", required = false) String user,
      @RequestParam(value = "company", required = false) String company,
      @RequestParam(value = "totalAmount", required = false) Long totalAmount,
      @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
      @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
      @RequestParam(value = "sort", required = false) String sort) {

    Page<OrderDto> orders = service.getPageOrders(user, company, totalAmount, dateFrom, dateTo, page, size, sort);

    return ResponseEntity.ok(orders);
  }

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
  public ResponseEntity<Void> changeOrder(@RequestBody List<OrdersGoodsDto> orderDto) {
    ordersGoodsService.changeOrdersGoodsByOrderId(orderDto);
    return ResponseEntity.status(OK).build();
  }

  @PostMapping("/add-goods")
  public ResponseEntity<Void> addGoodsToOrder(@RequestBody List<AddGoodToOrderDto> addGoodToOrderList) {
    ordersGoodsService.addGoodToOrdersGoods(addGoodToOrderList);
    return ResponseEntity.status(CREATED).build();
  }

  @GetMapping("/goods")
  public ResponseEntity<List<OrdersGoodsDto>> getOrdersGoods(@RequestParam Long orderId) {
    return ResponseEntity.ok(ordersGoodsService.getOrdersGoodsByOrderId(orderId));
  }

  @PostMapping("/delete-good")
  public ResponseEntity<Void> deleteGood(@RequestParam Long ordersGoodsId) {
    ordersGoodsService.deleteOrdersGoodsByOrderId(ordersGoodsId);
    return ResponseEntity.status(OK).build();
  }
}
