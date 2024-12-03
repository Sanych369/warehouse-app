package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.damrin.app.db.repository.OrdersRepository;
import ru.damrin.app.db.repository.StoreRepository;
import ru.damrin.app.mapper.StoreMapper;
import ru.damrin.app.service.ReportGeneratorService;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

  private final StoreMapper storeMapper;
  private final   StoreRepository storeRepository;
  private final OrdersRepository ordersRepository;
  private final ReportGeneratorService reportGeneratorService;

  private static final String SALE_REPORT_NAME = "Отчёт по продажам %s - %s";
  private static final String STORE_REPORT_NAME = "Отчёт по движениям товаров %s - %s";

  @GetMapping("/sale/my")
  public byte[] getSaleReportByCurrentUser(LocalDate from, LocalDate to) {
    //TODO: взять из сесурьки
    var userId = 1L;
    final var orderSet = ordersRepository.findAllByUserIdAndCreatedAtBetween(userId, from, to);
    return reportGeneratorService.generateSaleReport(orderSet, String.format(SALE_REPORT_NAME, from, to));
  }

  @GetMapping("/sale/all")
  public byte[] getSaleReportByAll(LocalDate from, LocalDate to) {
    //TODO вернуть в ДТО sale reports
    final var orderSet = ordersRepository.findAllByCreatedAtBetween(from, to);
    return reportGeneratorService.generateSaleReport(orderSet, String.format(SALE_REPORT_NAME, from, to));
  }

  @GetMapping("/store")
  public byte[] getStoreReport(LocalDate from, LocalDate to) {

    var storeSet = storeMapper.toDto(storeRepository.findAllByOrderByCreatedAtDesc());
    return reportGeneratorService.generateStoreReport(storeSet, String.format(STORE_REPORT_NAME, from, to));
  }

}
