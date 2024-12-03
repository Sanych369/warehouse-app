package ru.damrin.app.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.OrderEntity;
import ru.damrin.app.model.StoreDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;

@Slf4j
@Service
public class ReportGeneratorService {

  private static final String MANAGER = "Менеджер";
  private static final String CUSTOMER = "Заказчик";
  private static final String CONTENT_OF_ORDER = "Содержание заказа";
  private static final String ORDER_SUM_TOTAL = "Сумма заказа";
  private static final String RESPONSIBLE_PERSON = "Ответственный";
  private static final String GOOD_NAME = "Наименование товара";
  private static final String PURCHASE_PRICE = "Закупочная цена";
  private static final String ARRIVAL_TOTAL = "Приход количество (шт.)";
  private static final String CONSUMPTION_TOTAL = "Списание количество (шт.)";
  private static final String REASON = "Обоснование";
  private static final String ARRIVAL_TOTAL_SUM = "Сумма прихода";
  private static final String CONSUMPTION_TOTAL_SUM = "Сумма списания";

  public byte[] generateSaleReport(Set<OrderEntity> orders, String sheetName) {
    var workbook = new XSSFWorkbook();
    var sheet = workbook.createSheet(String.format(sheetName));

    var headerStyle = headerCellStyle(workbook);
    var header = sheet.createRow(0);

    var headerCell = headerCellDate(header, headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue(MANAGER);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(2);
    headerCell.setCellValue(CUSTOMER);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(3);
    headerCell.setCellValue(CONTENT_OF_ORDER);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(4);
    headerCell.setCellValue(ORDER_SUM_TOTAL);
    headerCell.setCellStyle(headerStyle);

    var style = cellStyle(workbook);
    var dateStyle = cellStyle(workbook);
    var creationHelper = new XSSFCreationHelper(workbook);
    var dateFormat = "yyyy-mm-dd";
    dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat(dateFormat));

    int rowNum = 1;
    int userNameMaxWidth = MANAGER.length();
    int companyNameMaxWidth = CUSTOMER.length();
    int orderGoodsMaxWidth = CONTENT_OF_ORDER.length();
    int totalAmountMaxWidth = ORDER_SUM_TOTAL.length();

    for (OrderEntity order : orders) {

      int cellNum = 0;
      Row row = sheet.createRow(rowNum++);

      Cell cell = row.createCell(cellNum++);
      cell.setCellValue(order.getCreatedAt());
      cell.setCellStyle(dateStyle);

      cell = row.createCell(cellNum++);
      var userName = order.getUser().getName() + " " + order.getUser().getSurname();
      cell.setCellValue(userName);
      cell.setCellStyle(style);
      userNameMaxWidth = Math.max(userName.length(), userNameMaxWidth);

      cell = row.createCell(cellNum++);
      var companyName = order.getCompany().getName();
      cell.setCellValue(companyName);
      cell.setCellStyle(style);
      companyNameMaxWidth = Math.max(companyName.length(), companyNameMaxWidth);

      cell = row.createCell(cellNum++);
      var joiningGoods = new StringBuilder();
      for (var goods : order.getOrdersGoods()) {
        String goodNameQuantity = goods.getGoodName() + " " + goods.getQuantity();
        orderGoodsMaxWidth = Math.max(goodNameQuantity.length(), orderGoodsMaxWidth);
        joiningGoods.append(goodNameQuantity).append("\n");
      }
      cell.setCellValue(joiningGoods.toString());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum, NUMERIC);
      var totalAmountString = order.getTotalAmount().toString();
      cell.setCellValue(totalAmountString);
      cell.setCellStyle(style);
      totalAmountMaxWidth = Math.max(totalAmountString.length(), totalAmountMaxWidth);

      row.setHeightInPoints(order.getOrdersGoods().size() * sheet.getDefaultRowHeightInPoints());
    }

    sheet.setColumnWidth(0, (dateFormat.length() + 2) * 256);
    sheet.setColumnWidth(1, (userNameMaxWidth + 2) * 256);
    sheet.setColumnWidth(2, (companyNameMaxWidth + 2) * 256);
    sheet.setColumnWidth(3, (orderGoodsMaxWidth + 2) * 256);
    sheet.setColumnWidth(4, (totalAmountMaxWidth + 2) * 256);

    return getBytesReport(sheetName, workbook);
  }

  public byte[] generateStoreReport(List<StoreDto> storeDtoSet, String sheetName) {
    var workbook = new XSSFWorkbook();

    var sheet = workbook.createSheet(String.format(sheetName));
    sheet.autoSizeColumn(0);

    var headerStyle = headerCellStyle(workbook);
    var header = sheet.createRow(0);

    var headerCell = headerCellDate(header, headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue(RESPONSIBLE_PERSON);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(2);
    headerCell.setCellValue(GOOD_NAME);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(3);
    headerCell.setCellValue(PURCHASE_PRICE);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(4);
    headerCell.setCellValue(ARRIVAL_TOTAL);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(5);
    headerCell.setCellValue(CONSUMPTION_TOTAL);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(6);
    headerCell.setCellValue(REASON);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(7);
    headerCell.setCellValue(ARRIVAL_TOTAL_SUM);
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(8);
    headerCell.setCellValue(CONSUMPTION_TOTAL_SUM);
    headerCell.setCellStyle(headerStyle);

    var style = cellStyle(workbook);
    var dateStyle = cellStyle(workbook);
    CreationHelper creationHelper = new XSSFCreationHelper(workbook);
    final var dateTimeFormat = "yyyy-mm-dd HH:mm:ss";
    dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat(dateTimeFormat));

    int rowNum = 1;
    int responsiblePersonMaxWidth = RESPONSIBLE_PERSON.length();
    int goodNameMaxWidth = GOOD_NAME.length();
    int purchasePriceMaxWidth = PURCHASE_PRICE.length();
    int arrivedTotalMaxWidth = ARRIVAL_TOTAL.length();
    int consumptionTotalMaxWidth = CONSUMPTION_TOTAL.length();
    int reasonMaxWidth = REASON.length();
    int arrivedTotalSum = ARRIVAL_TOTAL_SUM.length();
    int consumptionTotalSum = CONSUMPTION_TOTAL_SUM.length();

    BigDecimal arrivalSum = BigDecimal.ZERO;
    BigDecimal consumptionSum = BigDecimal.ZERO;

    for (StoreDto storeDto : storeDtoSet) {

      Row row = sheet.createRow(rowNum++);
      int cellNum = 0;

      Cell cell = row.createCell(cellNum++);
      cell.setCellValue(storeDto.createdAt());
      cell.setCellStyle(dateStyle);

      cell = row.createCell(cellNum++);
      var responsiblePerson = storeDto.responsiblePerson();
      cell.setCellValue(responsiblePerson);
      responsiblePersonMaxWidth = Math.max(responsiblePerson.length(), responsiblePersonMaxWidth);
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      var goodName = storeDto.goodName();
      cell.setCellValue(goodName);
      goodNameMaxWidth = Math.max(goodName.length(), goodNameMaxWidth);
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++, NUMERIC);
      var purchasePrice = storeDto.purchasePrice().toString();
      cell.setCellValue(purchasePrice);
      purchasePriceMaxWidth = Math.max(purchasePrice.length(), purchasePriceMaxWidth);
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++, NUMERIC);
      var arrived = storeDto.arrivedTotal().toString();
      cell.setCellValue(arrived);
      arrivedTotalMaxWidth = Math.max(arrived.length(), arrivedTotalMaxWidth);
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++, NUMERIC);
      var consumption = storeDto.consumptionTotal().toString();
      cell.setCellValue(consumption);
      consumptionTotalMaxWidth = Math.max(consumption.length(), consumptionTotalMaxWidth);
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      var reason = storeDto.reason();
      cell.setCellValue(reason);
      reasonMaxWidth = Math.max(reason.length(), reasonMaxWidth);
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++, NUMERIC);
      var arrivalTotal = storeDto.purchasePrice()
          .multiply(BigDecimal.valueOf(storeDto.arrivedTotal()));
      arrivalSum = arrivalSum.add(arrivalTotal);
      cell.setCellValue(arrivalTotal.toString());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum, NUMERIC);
      var consumptionTotal = storeDto.purchasePrice()
          .multiply(BigDecimal.valueOf(storeDto.consumptionTotal()));
      consumptionSum = consumptionSum.add(consumptionTotal);
      cell.setCellValue(consumptionTotal.toString());
      cell.setCellStyle(style);
    }

    var row = sheet.createRow(rowNum);

    Cell cell = row.createCell(0);
    cell.setCellValue("Итого");
    cell.setCellStyle(style);

    cell = row.createCell(7, NUMERIC);
    var totalArrival = arrivalSum.toString();
    cell.setCellValue(totalArrival);
    arrivedTotalSum = Math.max(totalArrival.length(), arrivedTotalSum);
    cell.setCellStyle(style);

    cell = row.createCell(8, NUMERIC);
    var totalCons = consumptionSum.toString();
    consumptionTotalSum = Math.max(totalCons.length(), consumptionTotalSum);
    cell.setCellValue(totalCons);
    cell.setCellStyle(style);

    sheet.setColumnWidth(0, (dateTimeFormat.length() + 2) * 256);
    sheet.setColumnWidth(1, (responsiblePersonMaxWidth + 2) * 256);
    sheet.setColumnWidth(2, (goodNameMaxWidth + 2) * 256);
    sheet.setColumnWidth(3, (purchasePriceMaxWidth + 2) * 256);
    sheet.setColumnWidth(4, (arrivedTotalMaxWidth + 2) * 256);
    sheet.setColumnWidth(5, (consumptionTotalMaxWidth + 2) * 256);
    sheet.setColumnWidth(6, (reasonMaxWidth + 2) * 256);
    sheet.setColumnWidth(7, (arrivedTotalSum + 2) * 256);
    sheet.setColumnWidth(8, (consumptionTotalSum + 2) * 256);

    return getBytesReport(sheetName, workbook);
  }

  private CellStyle headerCellStyle(XSSFWorkbook workbook) {
    var headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);
    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    XSSFFont font = workbook.createFont();
    font.setFontName("ComicSansMs");
    font.setBold(true);
    headerStyle.setFont(font);
    return headerStyle;
  }

  private Cell headerCellDate(Row header, CellStyle headerStyle) {
    var headerCell = header.createCell(0);
    headerCell.setCellValue("Дата");
    headerCell.setCellStyle(headerStyle);
    return headerCell;
  }

  private CellStyle cellStyle(XSSFWorkbook workbook) {
    var style = workbook.createCellStyle();
    style.setWrapText(false);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setLeftBorderColor(IndexedColors.BLACK.index);
    style.setBorderRight(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.index);
    style.setBorderTop(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.index);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.index);
    style.setBorderBottom(BorderStyle.THIN);
    return style;
  }

  private byte[] getBytesReport(String sheetName, XSSFWorkbook workbook) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);
      workbook.close();
      return outputStream.toByteArray();
    } catch (IOException e) {
      log.error("Error while generating report. Cause: {}", e.getMessage());
      throw new WarehouseAppException(String.format("Ошибка в генерации отчёта %s", sheetName));
    }
  }
}
