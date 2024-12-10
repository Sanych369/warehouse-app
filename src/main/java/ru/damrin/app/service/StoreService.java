package ru.damrin.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.StoreRepository;
import ru.damrin.app.mapper.StoreMapper;
import ru.damrin.app.model.StoreDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final GoodRepository goodRepository;
  private final StoreMapper storeMapper;
  private final SortService sortService;

  public List<StoreDto> getAllStore() {
    return storeMapper.toDto(storeRepository.findAllByOrderByCreatedAtDesc());
  }

  @Transactional
  public void goodArrival(StoreDto storeDto, UserEntity user) {
    var store = storeMapper.toEntity(storeDto);

    store.setUser(user);
    store.setResponsiblePerson(user.getName() + " " + user.getSurname());

    var good = goodRepository.findByName(storeDto.goodName())
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Товар с наименованием %s не найден. Проверьте правильность", storeDto.goodName())));

    good.setBalance(good.getBalance() + storeDto.arrivedTotal());
    good.setPurchasePrice(storeDto.purchasePrice());
    store.setGood(good);
    storeRepository.save(store);
  }

  @Transactional
  public void goodConsumption(StoreDto storeDto, UserEntity user) {
    var store = storeMapper.toEntity(storeDto);
    store.setUser(user);
    store.setResponsiblePerson(user.getName() + " " + user.getSurname());

    var good = goodRepository.findByName(storeDto.goodName())
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Товар с наименованием %s не найден. Проверьте правильность", storeDto.goodName())));
    good.setBalance(good.getBalance() - storeDto.consumptionTotal());
    store.setGood(good);
    storeRepository.save(store);
  }

  public Page<StoreDto> getStorePage(String goodName, BigDecimal purchasePrice, Long arrivedTotal, Long consumptionTotal,
                                     String reason, LocalDateTime dateFrom, LocalDateTime dateTo, int page, int size, String sort) {
    final var pageable = PageRequest.of(page, size, sortService.getSortOrderForStoreHistory(sort));
    return storeRepository.findAllWithFilters(goodName, purchasePrice, arrivedTotal, consumptionTotal, reason, dateFrom, dateTo, pageable)
        .map(storeMapper::toDto);

  }
}
