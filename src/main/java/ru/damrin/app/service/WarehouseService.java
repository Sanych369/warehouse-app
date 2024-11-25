package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.damrin.app.db.entity.Order;
import ru.damrin.app.db.repository.CategoryEntityRepository;
import ru.damrin.app.db.repository.OrdersRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

  private final CategoryEntityRepository repository;
  private final OrdersRepository ordersRepository;
  private final ReportGeneratorService reportGeneratorService;

  @Scheduled(fixedRate = 5000)
  public void createFile() throws IOException {
    File file = new File("warehouse.xlsx");
    if (!file.exists()) {
      file.createNewFile();
    }
    Set<Order> orderSet = ordersRepository.findAllByCreatedAtBefore(LocalDate.now());
    byte[] bytes = reportGeneratorService.generateSaleReport(orderSet, "Отчёт");
    Files.write(Path.of(file.getPath()), bytes);
    log.info("End");
  }

}
