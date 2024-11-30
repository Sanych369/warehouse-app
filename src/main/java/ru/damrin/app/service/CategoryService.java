package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.repository.CategoryRepository;
import ru.damrin.app.mapper.CategoryMapper;
import ru.damrin.app.model.category.CategoryDto;
import ru.damrin.app.model.category.CategoryRequest;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryMapper mapper;
  private final CategoryRepository repository;

  public List<CategoryDto> getAllCategories() {
    return repository.findAll().stream()
        .map(mapper::toDto)
        .toList();
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
        .orElseThrow(() -> new WarehouseAppException("Category not found"));
    var categoryName = category.getName();

    log.info("Started change category process from {} to name: {}", categoryName, categoryDto.name());

    mapper.toEntity(categoryDto);
    category.setName(categoryName);
    category.setMarkupPercentage(categoryDto.markupPercentage());

    if (request.needRecalculation()) {
      repository.saveAndFlush(category);
    } else {
      repository.save(category);
    }
    log.info("Category name {} was saved", categoryName);
  }
}