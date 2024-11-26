package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.repository.CategoryEntityRepository;
import ru.damrin.app.mapper.CategoryMapper;
import ru.damrin.app.model.CategoryDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryMapper categoryMapper;
  private final CategoryEntityRepository categoryRepository;

  public List<CategoryDto> getAllCategories() {
    return categoryMapper.entityToDtoList(categoryRepository.findAll());
  }

  public void addCategory(CategoryDto categoryDto) {
    log.info("Add category: {} with percentage: {}", categoryDto.name(), categoryDto.markupPercentage());
    var category = categoryMapper.dtoToEntity(categoryDto);
    var entity = categoryRepository.save(category);
    log.info("Added category: {} for id {}", category, entity.getId());
  }

  public void deleteCategoryById(Long id) {
    log.info("Delete category by id: {}", id);
    categoryRepository.deleteById(id);
  }

  public void changeCategory(CategoryDto categoryDto) {
    var category = categoryRepository.findById(categoryDto.id())
        .orElseThrow(() -> new WarehouseAppException("Category not found"));
    var categoryName = category.getName();
    log.info("Started change category process from {} to name: {}", categoryName, categoryDto.name());
    categoryMapper.dtoToEntity(categoryDto);
    category.setName(categoryName);
    category.setMarkupPercentage(categoryDto.markupPercentage());
    categoryRepository.save(category);
    log.info("Category name {} was saved", categoryName);
  }
}