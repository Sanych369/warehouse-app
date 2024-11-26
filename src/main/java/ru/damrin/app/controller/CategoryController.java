package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.model.CategoryDto;
import ru.damrin.app.service.CategoryService;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDto categoryDto) {
        checkCategoryDto(categoryDto);
        categoryService.addCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCategoryById(@RequestParam Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change")
    public ResponseEntity<Void> changeCategory(@RequestBody CategoryDto categoryDto) {
        checkCategoryDto(categoryDto);
        categoryService.changeCategory(categoryDto);
        return ResponseEntity.ok().build();
    }

    private void checkCategoryDto(CategoryDto categoryDto) {
        if (StringUtils.isEmpty(categoryDto.name())) {
            throw new WarehouseAppException("Category name and percentage cannot be empty");
        }

        if (isNull(categoryDto.markupPercentage())
            || categoryDto.markupPercentage().compareTo(BigDecimal.ZERO) < 0) {
            throw new WarehouseAppException("Category markup percentage cannot be zero or negative");
        }
    }
}
