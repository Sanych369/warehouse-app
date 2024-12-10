package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.model.StoreDto;
import ru.damrin.app.service.StoreService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для управления складом.
 */
@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @PostMapping("/arrival")
  public ResponseEntity<Void> goodsArrival(@RequestBody StoreDto storeDto, @AuthenticationPrincipal UserEntity user) {
    storeService.goodArrival(storeDto, user);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/consumption")
  public ResponseEntity<Void> goodsConsumption(@RequestBody StoreDto storeDto, @AuthenticationPrincipal UserEntity user) {
    storeService.goodConsumption(storeDto, user);
    return ResponseEntity.ok().build();
  }

  @GetMapping("all")
  public ResponseEntity<List<StoreDto>> getAllStore() {
    return ResponseEntity.ok(storeService.getAllStore());
  }

  @GetMapping("/page")
  public ResponseEntity<Page<StoreDto>> getStorePage(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "goodName", required = false) String goodName,
      @RequestParam(value = "purchasePrice", required = false) BigDecimal purchasePrice,
      @RequestParam(value = "arrivedTotal", required = false) Long arrivedTotal,
      @RequestParam(value = "consumptionTotal", required = false) Long consumptionTotal,
      @RequestParam(value = "reason", required = false) String reason,
      @RequestParam(value = "dateFrom", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateFrom,
      @RequestParam(value = "dateTo", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateTo,
      @RequestParam(value = "sort", required = false) String sort) {

    final var storePage = storeService.getStorePage(goodName, purchasePrice, arrivedTotal, consumptionTotal,
        reason, dateFrom, dateTo, page, size, sort);

    return ResponseEntity.ok(storePage);
  }

}
