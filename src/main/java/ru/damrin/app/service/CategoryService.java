package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.repository.CategoryRepository;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.mapper.CategoryMapper;
import ru.damrin.app.model.category.CategoryDto;
import ru.damrin.app.model.category.CategoryRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryMapper mapper;
  private final CategoryRepository repository;
  private final GoodRepository goodRepository;

  public Page<CategoryDto> getAllCategories(String name, BigDecimal markupPercentage, int page, int size, Sort sort) {
    Pageable pageable = PageRequest.of(page, size, sort != null ? sort : Sort.unsorted());

    // Применяем фильтры и сортировку
    if (name != null && markupPercentage != null) {
      return repository.findByNameContainingAndMarkupPercentage(name, markupPercentage, pageable).map(mapper::toDto);
    } else if (name != null) {
      return repository.findByNameContaining(name, pageable).map(mapper::toDto);
    } else if (markupPercentage != null) {
      return repository.findByMarkupPercentage(markupPercentage, pageable).map(mapper::toDto);
    } else {
      return repository.findAll(pageable).map(mapper::toDto);
    }
  }

  public void addCategory(CategoryDto categoryDto) {
    log.info("Add category: {} with percentage: {}", categoryDto.name(), categoryDto.markupPercentage());
    var category = mapper.toEntity(categoryDto);
    var entity = repository.save(category);
    log.info("Added category: {} for id {}", category, entity.getId());
  }

  public void deleteCategoryById(Long id) {
    log.info("Delete category by id: {}", id);
    repository.deleteById(id);
  }

  public void changeCategory(CategoryRequest request) {
    var categoryDto = request.categoryDto();
    var category = repository.findById(request.categoryDto().id())
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Категория товара: %s не найдена. Проверьте правильность и наличие данной категории", request.categoryDto().name())));

    var categoryName = category.getName();

    if (request.needRecalculation() && categoryDto.markupPercentage().compareTo(category.getMarkupPercentage()) != 0) {
      category.getGoods().forEach(good -> recalculate(good, categoryDto.markupPercentage()));
    }

    mapper.partialUpdate(categoryDto, category);
    repository.save(category);
    log.info("Category {} was changed", categoryName);
  }

  private void recalculate(GoodEntity good, BigDecimal newMarkupPercentage) {
    good.setSalePrice(good.getPurchasePrice().add(
        good.getPurchasePrice()
            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
            .multiply(newMarkupPercentage)).longValue());
  }
}