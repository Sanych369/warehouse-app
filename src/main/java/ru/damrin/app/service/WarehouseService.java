package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.enums.GoodCategory;
import ru.damrin.app.db.entity.CategoryEntity;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.repository.CategoryEntityRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

  private final CategoryEntityRepository repository;

  @Scheduled(fixedDelay = 5000L)
  public void someMethod() {
    CategoryEntity category = CategoryEntity.builder()
      .goods(List.of(
          GoodEntity.builder()
              .name("Карандаш берлинго")
              .price(BigDecimal.valueOf(150.00))
              .quantity(5L)
              .build(),
          GoodEntity.builder()
              .name("Ручка берлинго")
              .price(BigDecimal.valueOf(50.00))
              .quantity(50L)
              .build(),
          GoodEntity.builder()
              .name("Бумага берлинго")
              .price(BigDecimal.valueOf(1500.00))
              .quantity(1L)
              .build(),
          GoodEntity.builder()
              .name("Хуета берлинго")
              .price(BigDecimal.valueOf(15000.00))
              .quantity(0L)
              .build()
      ))
      .category(GoodCategory.PEN)
      .build();

    repository.save(category);
    log.info("Pizdec");

  }

}
