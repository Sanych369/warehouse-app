package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.damrin.app.db.entity.OrderEntity;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.db.repository.OrdersRepository;
import ru.damrin.app.db.repository.StoreRepository;
import ru.damrin.app.service.ReportGeneratorService;

import java.time.LocalDate;
import java.util.Set;

/**
 * Контроллер для получения отчётов.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

  private final StoreRepository storeRepository;
  private final OrdersRepository ordersRepository;
  private final ReportGeneratorService reportGeneratorService;

  private static final String SALE_REPORT_NAME = "Sales %s - %s";
  private static final String STORE_REPORT_NAME = "Goods %s - %s";

  @GetMapping("/sale/my")
  public ResponseEntity<byte[]> getSaleReportByCurrentUser(@RequestParam LocalDate from,
                                                           @RequestParam LocalDate to,
                                                           @AuthenticationPrincipal UserEntity user) {
    var userId = user.getId();
    final var orderSet = ordersRepository.findAllByUserIdAndCreatedAtBetween(userId, from, to);
    return getStoreResponse(from, to, orderSet);
  }

  @GetMapping("/sale/all")
  public ResponseEntity<byte[]> getSaleReportByAll(@RequestParam LocalDate from, @RequestParam LocalDate to) {
    var orderSet = ordersRepository.findAllByCreatedAtBetween(from, to);
    return getStoreResponse(from, to, orderSet);
  }

  private ResponseEntity<byte[]> getStoreResponse(@RequestParam LocalDate from, @RequestParam LocalDate to,
                                                  Set<OrderEntity> orderSet) {

    final var reportFileName = String.format(SALE_REPORT_NAME, from, to);
    byte[] report = reportGeneratorService.generateSaleReport(orderSet, reportFileName);

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDisposition(ContentDisposition.attachment()
        .filename(reportFileName + ".xlsx")
        .build());

    return ResponseEntity.ok()
        .headers(headers)
        .body(report);
  }

  @GetMapping("/store")
  public ResponseEntity<byte[]> getStoreReport(@RequestParam LocalDate from, @RequestParam LocalDate to) {
    var stores = storeRepository.findAllByOrderByCreatedAtDesc();
    final var reportFileName = String.format(STORE_REPORT_NAME, from, to);
    byte[] report = reportGeneratorService.generateStoreReport(stores, reportFileName);

    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDisposition(ContentDisposition.attachment()
        .filename(reportFileName + ".xlsx")
        .build());

    return ResponseEntity.ok()
        .headers(headers)
        .body(report);
  }
}
