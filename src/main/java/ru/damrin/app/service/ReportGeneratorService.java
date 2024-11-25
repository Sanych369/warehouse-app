package ru.damrin.app.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.Order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReportGeneratorService {
  //TODO: "Отчёт %s-%s"
  public byte[] generateSaleReport(Set<Order> orders, String sheetName) {
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
    for (Order order : orders) {

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
          .flatMap(x -> Stream.of(x.getGoodEntities().getName() + " " + x.getQuantity()))
          .collect(Collectors.joining("\n")));
      cell.setCellStyle(style);

      cell = row.createCell(cellNum);
      cell.setCellValue(order.getOrdersGoods().stream()
          .map(x -> x.getGoodEntities().getSalePrice().multiply(BigDecimal.valueOf(x.getQuantity())))
          .reduce(BigDecimal.ZERO, BigDecimal::add).toString());
      cell.setCellStyle(style);

      row.setHeightInPoints(order.getOrdersGoods().size() * sheet.getDefaultRowHeightInPoints());
      row.setRowStyle(style);
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
      throw new WarehouseAppException(e.getMessage());
    }
  }
}
