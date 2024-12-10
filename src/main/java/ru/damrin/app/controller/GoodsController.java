package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.damrin.app.model.good.GoodDto;
import ru.damrin.app.service.GoodService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Контроллер для управления товарами.
 */
@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

  private final GoodService goodService;

  @GetMapping("/page")
  public ResponseEntity<Page<GoodDto>> getGoods(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) BigDecimal purchasePrice,
      @RequestParam(required = false) Long salePrice,
      @RequestParam(required = false) Long balance,
      @RequestParam(required = false) String sort) {

    final var goods = goodService.getGoods(name, category, purchasePrice, salePrice, balance, sort, page, size);
    return ResponseEntity.ok(goods);
  }

  @GetMapping("/goods-for-order")
  public ResponseEntity<Page<GoodDto>> getGoodsForOrder(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) Long price,
      @RequestParam(required = false) Long balance) {
    final var goods = goodService.getGoodsForOrder(name, categoryId,  price, balance, page, size);
    return ResponseEntity.ok(goods);
  }

  @GetMapping("/list")
  public List<GoodDto> getAllGoods() {
    return goodService.getAllGoods();
  }

  @PostMapping("/add")
  public ResponseEntity<Void> addGood(@RequestBody GoodDto goodDto) {
    goodService.addGood(goodDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/edit")
  public ResponseEntity<Void> editGood(@RequestBody GoodDto goodDto) {
    goodService.updateGood(goodDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteGoodById(@RequestParam Long id) {
    goodService.deleteGoodById(id);
    return ResponseEntity.noContent().build();
  }
}
