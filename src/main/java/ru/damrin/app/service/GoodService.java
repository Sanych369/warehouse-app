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
import ru.damrin.app.mapper.GoodMapper;
import ru.damrin.app.model.good.GoodDto;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodService {

  private final CategoryRepository categoryRepository;
  private final GoodRepository goodRepository;
  private final SortService sortService;
  private final GoodMapper mapper;

  public List<GoodDto> getAllGoods() {
    return goodRepository.findAll().stream().map(mapper::toDto).toList();
  }

  public Page<GoodDto> getGoodsForOrder(String name, Long categoryId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return goodRepository.findByFiltersForOrder(name, categoryId, pageable)
        .map(mapper::toDto);
  }

  public Page<GoodDto> getGoods(String name,
                                String category,
                                BigDecimal purchasePrice,
                                Long salePrice,
                                Long balance,
                                String sort,
                                int page,
                                int size) {

    final var sortOrder = sortService.getSortOrderForGoods(sort);
    final var pageable = PageRequest.of(page, size, sortOrder != null ? sortOrder : Sort.unsorted());

    return goodRepository.findByFilters(name, category, purchasePrice, salePrice, balance, pageable)
        .map(mapper::toDto);
  }

  public void addGood(GoodDto goodDto) {
    log.info("Start add new good with name: {}", goodDto.name());
    final var category = categoryRepository.findById(goodDto.category().id())
        .orElseThrow(() -> new WarehouseAppException("Категория не найдена"));

    final var good = GoodEntity.builder()
        .name(goodDto.name())
        .category(category)
        .purchasePrice(goodDto.purchasePrice())
        .build();

    goodRepository.save(good);
    log.info("End add new good with name: {}", goodDto.name());
  }

  public void updateGood(GoodDto goodDto) {
    log.info("Start update good");
    var good = goodRepository.findById(goodDto.id())
        .orElseThrow(() -> new WarehouseAppException("Товар не найден"));
    final var goodName = good.getName();
    log.info("Update good: {}", goodName);
    mapper.partialUpdate(goodDto, good);
    goodRepository.save(good);
    log.info("End update good with name: {}", goodName);
  }

  public void deleteGoodById(Long id) {
    goodRepository.deleteById(id);
  }
}