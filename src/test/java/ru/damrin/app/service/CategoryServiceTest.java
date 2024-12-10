package ru.damrin.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.damrin.app.db.entity.CategoryEntity;
import ru.damrin.app.db.repository.CategoryRepository;
import ru.damrin.app.mapper.CategoryMapper;
import ru.damrin.app.model.category.CategoryRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.PrepareUtil.CATEGORY_ID;
import static utils.PrepareUtil.CATEGORY_NAME;
import static utils.PrepareUtil.MARKUP_PERCENTAGE;
import static utils.PrepareUtil.prepareCategory;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Spy
  private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

  @Mock
  private SortService sortService;

  @Mock
  private CategoryRepository repository;

  @InjectMocks
  private CategoryService categoryService;

  @Captor
  private ArgumentCaptor<CategoryEntity> changedEntityCaptor;

  @Test
  void getAllCategories() {
    categoryService.getAllCategories();

    verify(repository, times(1)).findAll();
    verify(mapper, times(1)).toDto(anyList());
  }

  @Test
  void getPageCategories() {
    when(repository.findByFilters(any(), any(), any())).thenReturn(mock());

    categoryService.getPageCategories(CATEGORY_NAME, MARKUP_PERCENTAGE, 1, 10, "name_desc");

    verify(sortService, times(1)).getSortOrderForCategory("name_desc");
    verify(repository, times(1)).findByFilters(any(), any(), any());
  }

  @Test
  void addCategory() {
    when(repository.save(any())).thenReturn(mock(CategoryEntity.class));

    categoryService.addCategory(prepareCategory());

    verify(repository, times(1)).save(any());
    verify(mapper, times(1)).toEntity(any());
  }

  @Test
  void deleteCategoryById() {

    categoryService.deleteCategoryById(CATEGORY_ID);

    verify(repository, times(1)).deleteById(CATEGORY_ID);
  }

  @Test
  void changeCategory() {
    when(repository.findById(any())).thenReturn(Optional.of(new CategoryEntity()));

    categoryService.changeCategory(new CategoryRequest(prepareCategory(), false));

    verify(repository, times(1)).findById(CATEGORY_ID);
    verify(repository, times(1)).save(changedEntityCaptor.capture());

    final var changedEntity = changedEntityCaptor.getValue();

    assertNotNull(changedEntity);
    assertEquals(CATEGORY_ID, changedEntity.getId());
    assertEquals(CATEGORY_NAME, changedEntity.getName());
    assertEquals(MARKUP_PERCENTAGE, changedEntity.getMarkupPercentage());
  }
}