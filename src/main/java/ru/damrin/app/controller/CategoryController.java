package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.model.category.CategoryDto;
import ru.damrin.app.model.category.CategoryRequest;
import ru.damrin.app.service.CategoryService;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Контроллер для управления категориями товаров.
 */
@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/list")
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    List<CategoryDto> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/page")
  public ResponseEntity<Page<CategoryDto>> getCategoriesPage(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "markupPercentage", required = false) BigDecimal markupPercentage,
      @RequestParam(value = "sort", required = false) String sort) {

    final var categories = categoryService.getPageCategories(name, markupPercentage, page, size, sort);
    return ResponseEntity.ok(categories);
  }

  @PostMapping("/add")
  public ResponseEntity<Void> addCategory(@RequestBody CategoryDto categoryDto) {
    checkCategoryDto(categoryDto);
    categoryService.addCategory(categoryDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteCategoryById(@RequestParam Long id) {
    categoryService.deleteCategoryById(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change")
  public ResponseEntity<Void> changeCategory(@RequestBody CategoryRequest request) {
    checkCategoryDto(request.categoryDto());
    categoryService.changeCategory(request);
    return ResponseEntity.ok().build();
  }

  private void checkCategoryDto(CategoryDto categoryDto) {
    if (isNull(categoryDto) || StringUtils.isEmpty(categoryDto.name()) || isNull(categoryDto.markupPercentage())) {
      throw new WarehouseAppException("Наименование категории и процент наценки не могут быть пусты. Заполните данные");
    }

    if (categoryDto.markupPercentage().compareTo(BigDecimal.ZERO) < 0) {
      throw new WarehouseAppException("Процент наценки не может быть ниже 0.");
    }
  }
}
