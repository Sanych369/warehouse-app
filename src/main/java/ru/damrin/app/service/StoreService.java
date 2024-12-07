package ru.damrin.app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.StoreRepository;
import ru.damrin.app.db.repository.UserRepository;
import ru.damrin.app.mapper.StoreMapper;
import ru.damrin.app.model.StoreDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final GoodRepository goodRepository;
  private final UserRepository userRepository;
  private final StoreMapper storeMapper;

  @Transactional
  public void goodArrival(StoreDto storeDto) {

    var store = storeMapper.toEntity(storeDto);
    //TODO: Юзера достать и отдать на пре персист
    //TODO: Подумать над передачей goodId чтобы найти и связать
    store.setUser(userRepository.findById(1L).get());
    var good = goodRepository.findByName(storeDto.goodName())
        .orElseThrow(() -> new WarehouseAppException(
                String.format("Товар с наименованием %s не найден. Проверьте правильность", storeDto.goodName())));

    good.setBalance(good.getBalance() + storeDto.arrivedTotal());
    good.setPurchasePrice(storeDto.purchasePrice());
    store.setGood(good);
    storeRepository.save(store);
  }

  @Transactional
  public void goodConsumption(StoreDto storeDto) {
    var store = storeMapper.toEntity(storeDto);
    //TODO: Юзера достать и отдать на пре персист
    store.setUser(userRepository.findById(1L).get());
    store.setResponsiblePerson("Какой то не мутный тип");
    var good = goodRepository.findByName(storeDto.goodName())
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Товар с наименованием %s не найден. Проверьте правильность", storeDto.goodName())));
    good.setBalance(good.getBalance() - storeDto.arrivedTotal());
    store.setGood(good);
    storeRepository.save(store);
  }

  public List<StoreDto> getAllStore() {
    return storeMapper.toDto(storeRepository.findAllByOrderByCreatedAtDesc());
  }
}
