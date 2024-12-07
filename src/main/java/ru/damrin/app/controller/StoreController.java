package ru.damrin.app.controller;
//TODO: только для кладовщика и админа

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.damrin.app.model.StoreDto;
import ru.damrin.app.service.StoreService;

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
  public ResponseEntity<Void> goodsArrival(StoreDto storeDto) {
    storeService.goodArrival(storeDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/consumption")
  public ResponseEntity<Void> goodsConsumption(StoreDto storeDto) {
    storeService.goodConsumption(storeDto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("all")
  public ResponseEntity<List<StoreDto>> getAllStore() {
    return ResponseEntity.ok(storeService.getAllStore());
  }
}
