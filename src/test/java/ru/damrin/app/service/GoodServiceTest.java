package ru.damrin.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.damrin.app.db.entity.CategoryEntity;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.repository.CategoryRepository;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.mapper.GoodMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.PrepareUtil.CATEGORY_ID;
import static utils.PrepareUtil.CATEGORY_NAME;
import static utils.PrepareUtil.GOOD_BALANCE;
import static utils.PrepareUtil.GOOD_ID;
import static utils.PrepareUtil.GOOD_NAME;
import static utils.PrepareUtil.PURCHASE_PRICE;
import static utils.PrepareUtil.SALE_PRICE;
import static utils.PrepareUtil.prepareGood;

@ExtendWith(MockitoExtension.class)
class GoodServiceTest {

  @Mock
  private GoodMapper mapper;

  @Mock
  private SortService sortService;

  @Mock
  private GoodRepository repository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private GoodService goodService;

  @Captor
  private ArgumentCaptor<GoodEntity> changedEntityCaptor;

  @Test
  void getAllGoods() {
    when(repository.findAll()).thenReturn(List.of(new GoodEntity()));

    goodService.getAllGoods();

    verify(repository, times(1)).findAll();
    verify(mapper, times(1)).toDto(any());
  }

  @Test
  void getGoodsForOrder() {
    when(repository.findByFiltersForOrder(any(), any(), any(), any(), any())).thenReturn(mock());

    goodService.getGoodsForOrder(GOOD_NAME, CATEGORY_ID, SALE_PRICE, GOOD_BALANCE, 1, 10);

    verify(repository, times(1)).findByFiltersForOrder(any(), any(), any(), any(), any());
  }

  @Test
  void getGoods() {
    when(repository.findByFilters(any(), any(), any(), any(), any(), any()))
        .then(invocationOnMock -> new PageImpl<>(List.of(new GoodEntity())));

    goodService.getGoods(GOOD_NAME, CATEGORY_NAME, PURCHASE_PRICE, SALE_PRICE, GOOD_BALANCE, "name_desc", 1, 10);

    verify(repository, times(1)).findByFilters(any(), any(), any(), any(), any(), any());
    verify(sortService, times(1)).getSortOrderForGoods(any());
  }

  @Test
  void addGood() {
    when(categoryRepository.findById(any())).thenReturn(Optional.of(mock(CategoryEntity.class)));

    goodService.addGood(prepareGood());

    verify(repository, times(1)).save(any());
  }

  @Test
  void updateGood() {

    when(repository.findById(any())).thenReturn(Optional.of(new GoodEntity()));

    goodService.updateGood(prepareGood());

    verify(repository, times(1)).findById(any());
    verify(repository, times(1)).save(changedEntityCaptor.capture());

    final var changedEntity = changedEntityCaptor.getValue();

    assertNotNull(changedEntity);

    verify(repository, times(1)).findById(any());
    verify(mapper, times(1)).partialUpdate(any(), any());
  }

  @Test
  void deleteGoodById() {

    goodService.deleteGoodById(GOOD_ID);

    verify(repository, times(1)).deleteById(any());
  }
}