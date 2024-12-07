package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.repository.CategoryRepository;
import ru.damrin.app.mapper.CategoryMapper;
import ru.damrin.app.model.category.CategoryDto;
import ru.damrin.app.model.category.CategoryRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryMapper mapper;
  private final GoodService goodService;
  private final SortService sortService;
  private final CategoryRepository repository;


  public List<CategoryDto> getAllCategories() {
    return mapper.toDto(repository.findAll());
  }

  public Page<CategoryDto> getPageCategories(String name, BigDecimal markupPercentage, int page, int size, String sort) {
    final var sortOrder = sortService.getSortOrderForCategory(sort);
    final var pageable = PageRequest.of(page, size, sortOrder != null ? sortOrder : Sort.unsorted());
    return repository.findByFilters(name, markupPercentage, pageable)
        .map(mapper::toDto);
  }

  public void addCategory(CategoryDto categoryDto) {
    log.info("Add category: {} with percentage: {}", categoryDto.name(), categoryDto.markupPercentage());
    final var category = mapper.toEntity(categoryDto);
    final var entity = repository.save(category);
    log.info("Added category: {} for id {}", categoryDto.name(), entity.getId());
  }

  public void deleteCategoryById(Long id) {
    log.info("Delete category by id: {}", id);
    repository.deleteById(id);
  }

  public void changeCategory(CategoryRequest request) {
    log.info("Starting change category");
    var categoryDto = request.categoryDto();
    var category = repository.findById(request.categoryDto().id())
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Категория товара: %s не найдена. Проверьте правильность и наличие данной категории",
                request.categoryDto().name())));

    var categoryName = category.getName();

    if (request.needRecalculation() && categoryDto.markupPercentage().compareTo(category.getMarkupPercentage()) != 0) {
      log.info("Starting recalculation sale price goods for category: {} to percentage: {}",
          categoryName, categoryDto.markupPercentage());
      category.getGoods().forEach(good -> recalculate(good, categoryDto.markupPercentage()));
    }

    mapper.partialUpdate(categoryDto, category);
    repository.save(category);
    log.info("Category {} was changed successfully", categoryName);
  }

  private void recalculate(GoodEntity good, BigDecimal newMarkupPercentage) {
    good.setSalePrice(good.getPurchasePrice().add(
        good.getPurchasePrice()
            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
            .multiply(newMarkupPercentage)).longValue());
  }
}