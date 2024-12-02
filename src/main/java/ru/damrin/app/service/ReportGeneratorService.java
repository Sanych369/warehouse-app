package ru.damrin.app.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReportGeneratorService {
  //TODO: "Отчёт %s-%s"
  public byte[] generateSaleReport(Set<OrderEntity> orders, String sheetName) {
    XSSFWorkbook workbook = new XSSFWorkbook();

    Sheet sheet = workbook.createSheet(String.format(sheetName));

    Row header = sheet.createRow(0);
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);
    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

    header.setRowStyle(headerStyle);

    XSSFFont font = workbook.createFont();
    font.setFontName("ComicSansMs");
    font.setBold(true);
    headerStyle.setFont(font);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("Дата");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue("Менеджер");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(2);
    headerCell.setCellValue("Заказчик");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(3);
    headerCell.setCellValue("Заказ");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(4);
    headerCell.setCellValue("Сумма");
    headerCell.setCellStyle(headerStyle);

    int rowNum = 1;
    for (OrderEntity order : orders) {

      int cellNum = 0;

      Row row = sheet.createRow(rowNum++);
      CellStyle style = workbook.createCellStyle();
      style.setWrapText(false);
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);

      CreationHelper creationHelper = new XSSFCreationHelper(workbook);
      style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd"));

      row.setRowStyle(style);

      Cell cell = row.createCell(cellNum++);
      cell.setCellValue(order.getCreatedAt());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(order.getUser().getName() + " " + order.getUser().getSurname());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(order.getCompany().getName());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(order.getOrdersGoods().stream()
          .flatMap(x -> Stream.of(x.getGoodName() + " " + x.getQuantity()))
          .collect(Collectors.joining("\n")));
      cell.setCellStyle(style);

      cell = row.createCell(cellNum);
      cell.setCellValue(order.getTotalAmount());
      cell.setCellStyle(style);

      row.setHeightInPoints(order.getOrdersGoods().size() * sheet.getDefaultRowHeightInPoints());
      row.setRowStyle(style); //TODO: проверить стайлы
    }

    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
    sheet.autoSizeColumn(2);
    sheet.autoSizeColumn(3);
    sheet.autoSizeColumn(4);
    sheet.autoSizeColumn(5);

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

  public byte[] generateStoreReport(List<StoreDto> storeDtoSet, String sheetName) {
    XSSFWorkbook workbook = new XSSFWorkbook();

    Sheet sheet = workbook.createSheet(String.format(sheetName));

    Row header = sheet.createRow(0);
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);
    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

    header.setRowStyle(headerStyle);

    XSSFFont font = workbook.createFont();
    font.setFontName("ComicSansMs");
    font.setBold(true);
    headerStyle.setFont(font);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("Дата");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue("Ответственный");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(2);
    headerCell.setCellValue("Наименование товара");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(3);
    headerCell.setCellValue("Закупочная цена");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(4);
    headerCell.setCellValue("Приход количество (шт.)");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(5);
    headerCell.setCellValue("Списание количество (шт.)");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(6);
    headerCell.setCellValue("Обоснование");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(7);
    headerCell.setCellValue("Сумма прихода");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(8);
    headerCell.setCellValue("Сумма списания");
    headerCell.setCellStyle(headerStyle);

    int rowNum = 1;
    for (StoreDto storeDto : storeDtoSet) {

      int cellNum = 0;

      Row row = sheet.createRow(rowNum++);
      CellStyle style = workbook.createCellStyle();
      style.setWrapText(false);
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      CreationHelper creationHelper = new XSSFCreationHelper(workbook);
      style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd HH:mm:ss"));

      row.setRowStyle(style);

      Cell cell = row.createCell(cellNum++);
      cell.setCellValue(storeDto.createdAt());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(storeDto.responsiblePerson());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(storeDto.goodName());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(String.valueOf(storeDto.purchasePrice()));
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(storeDto.arrivedTotal());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(storeDto.consumptionTotal());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue(storeDto.reason());
      cell.setCellStyle(style);

      cell = row.createCell(cellNum++);
      cell.setCellValue((RichTextString) storeDto.purchasePrice() //TODO: багуется - форматирует в дату
          .multiply(BigDecimal.valueOf(storeDto.arrivedTotal())));
      cell.setCellStyle(style);

      cell = row.createCell(cellNum);
      cell.setCellValue(storeDto.purchasePrice()
          .multiply(BigDecimal.valueOf(storeDto.consumptionTotal())).floatValue());
      cell.setCellStyle(style);

    }

    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
    sheet.autoSizeColumn(2);
    sheet.autoSizeColumn(3);
    sheet.autoSizeColumn(4);
    sheet.autoSizeColumn(5);
    sheet.autoSizeColumn(6);
    sheet.autoSizeColumn(7);
    sheet.autoSizeColumn(8);

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
