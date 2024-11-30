package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.damrin.app.model.order.CreateOrderDto;
import ru.damrin.app.model.order.OrderDto;
import ru.damrin.app.service.OrderService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final OrderService service;

//  @GetMapping("/my")
//  public ResponseEntity<List<OrderDto>> getMyOrder() {
////    тут возврат по юзеру из контекста сесурити
//    long userId = 1L;
//    return ResponseEntity.ok(service.findAllByUserId(userId));
//  }
//
//  @GetMapping("/all")
//  public ResponseEntity<List<OrderDto>> getAllOrders() {
//    return ResponseEntity.ok(service.findAll());
//  }

  @PostMapping("/create")
  public ResponseEntity<Void> createOrder(@RequestBody CreateOrderDto createOrderDto) {
    service.createOrder(createOrderDto);
    return ResponseEntity.status(CREATED).build();
  }
}
