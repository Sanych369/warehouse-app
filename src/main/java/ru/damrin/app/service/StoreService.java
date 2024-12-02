package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.StoreRepository;
import ru.damrin.app.mapper.StoreMapper;
import ru.damrin.app.model.StoreDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final GoodRepository goodRepository;
  private final StoreMapper storeMapper;

  public void goodArrival(StoreDto storeDto) {

    var store = storeMapper.toEntity(storeDto);
    //TODO: ФИО из сесурьки
    store.setResponsiblePerson("Какой то не мутный тип");
    var good = goodRepository.findByName(store.getGoodName())
        .orElseThrow(() -> new WarehouseAppException("Товар с наименованием %s не найден. Проверьте правильность"));

    good.setBalance(good.getBalance() + storeDto.arrivedTotal());
    good.setPurchasePrice(storeDto.purchasePrice());
    goodRepository.save(good);
    storeRepository.save(store);
  }

  public void goodConsumption(StoreDto storeDto) {
    var store = storeMapper.toEntity(storeDto);
    //TODO: ФИО из сесурьки
    store.setResponsiblePerson("Какой то не мутный тип");
    var good = goodRepository.findByName(store.getGoodName())
        .orElseThrow(() -> new WarehouseAppException("Товар с наименованием %s не найден. Проверьте правильность"));

    good.setBalance(good.getBalance() - storeDto.arrivedTotal());;
    goodRepository.save(good);
    storeRepository.save(store);
  }

  public List<StoreDto> getAllStore() {
    return storeMapper.toDto(storeRepository.findAllByOrderByCreatedAtDesc());
  }
}
