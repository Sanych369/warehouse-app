package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.repository.CategoryRepository;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.mapper.GoodMapper;
import ru.damrin.app.model.good.GoodDto;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GoodService {

  private final CategoryRepository categoryRepository;
  private final GoodRepository goodRepository;
  private final SortService sortService;
  private final GoodMapper mapper;

  public Page<GoodDto> getGoods(String name,
                                String category,
                                BigDecimal purchasePrice,
                                Long salePrice,
                                Long balance,
                                String sort,
                                int page,
                                int size) {

    Sort sortOrder = sortService.getSortOrderForGoods(sort);
    Pageable pageable = PageRequest.of(page, size, sortOrder != null
        ? sortOrder
        : Sort.unsorted());
    return goodRepository.findByFilters(name, category, purchasePrice, salePrice, balance, pageable)
        .map(mapper::toDto);
  }

  public void addGood(GoodDto goodDto) {
    var category = categoryRepository.findById(goodDto.category().id())
        .orElseThrow(() -> new WarehouseAppException("Категория не найдена"));

    var good = GoodEntity.builder()
        .name(goodDto.name())
        .category(category)
        .balance(0L)
        .purchasePrice(goodDto.purchasePrice())
        .build();
    goodRepository.save(good);
  }

  public void updateGood(GoodDto goodDto) {
    GoodEntity good = goodRepository.findById(goodDto.id())
        .orElseThrow(() -> new WarehouseAppException("Товар не найден"));
    var category = categoryRepository.findById(goodDto.category().id())
        .orElseThrow(() -> new WarehouseAppException("Категория не найдена"));
    good.setName(goodDto.name());
    good.setPurchasePrice(goodDto.purchasePrice());
    good.setSalePrice(goodDto.salePrice());
    good.setCategory(category);
    goodRepository.save(good);
  }

  public void deleteGoodById(Long id) {
    goodRepository.deleteById(id);
  }
}