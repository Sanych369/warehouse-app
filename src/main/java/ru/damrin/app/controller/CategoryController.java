package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

import static java.util.Objects.isNull;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/list")
  public ResponseEntity<Page<CategoryDto>> getCategories(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "markupPercentage", required = false) BigDecimal markupPercentage,
      @RequestParam(value = "sort", required = false) String sort) {

    Sort sortOrder;

    switch (sort) {
      case "name_desc" -> sortOrder = Sort.by(desc("name"));
      case "markup_asc" -> sortOrder = Sort.by(asc("markupPercentage"));
      case "markup_desc" -> sortOrder = Sort.by(desc("markupPercentage"));
      default -> sortOrder = Sort.by(asc("name"));
    }

    Page<CategoryDto> categories = categoryService.getAllCategories(name, markupPercentage, page, size, sortOrder);

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

  // CATEGORY + BOOL
  @PostMapping("/change")
  public ResponseEntity<Void> changeCategory(@RequestBody CategoryRequest request) {
    checkCategoryDto(request.categoryDto());
    categoryService.changeCategory(request);
    return ResponseEntity.ok().build();
  }

  private void checkCategoryDto(CategoryDto categoryDto) {
    if (isNull(categoryDto) || StringUtils.isEmpty(categoryDto.name()) || isNull(categoryDto.markupPercentage())) {
      throw new WarehouseAppException("Category name and percentage cannot be empty");
    }

    if (categoryDto.markupPercentage().compareTo(BigDecimal.ZERO) < 0) {
      throw new WarehouseAppException("Category markup percentage cannot be zero or negative");
    }
  }
}
