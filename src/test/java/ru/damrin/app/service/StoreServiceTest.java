package ru.damrin.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.entity.StoreEntity;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.StoreRepository;
import ru.damrin.app.mapper.StoreMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.PrepareUtil.GOOD_BALANCE;
import static utils.PrepareUtil.prepareStoreDto;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private GoodRepository goodRepository;

  @Spy
  private StoreMapper storeMapper = Mappers.getMapper(StoreMapper.class);

  @InjectMocks
  private StoreService storeService;

  private static final Long ARRIVED = 1000L;
  private static final Long CONSUMPTION = 500L;

  @Test
  void goodArrival() {
    when(goodRepository.findByName(any())).thenReturn(Optional.of(GoodEntity.builder().balance(GOOD_BALANCE).build()));

    storeService.goodArrival(prepareStoreDto(ARRIVED, 0L), new UserEntity());

    verify(goodRepository, times(1)).findByName(any());
    verify(storeRepository, times(1)).save(any());
  }

  @Test
  void goodConsumption() {
    when(goodRepository.findByName(any())).thenReturn(Optional.of(GoodEntity.builder().balance(GOOD_BALANCE).build()));

    storeService.goodConsumption(prepareStoreDto(0L, CONSUMPTION), new UserEntity());

    verify(goodRepository, times(1)).findByName(any());
    verify(storeRepository, times(1)).save(any());
  }

  @Test
  void getAllStore() {
    when(storeRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(new StoreEntity()));

    storeService.getAllStore();

    verify(storeRepository, times(1)).findAllByOrderByCreatedAtDesc();
    verify(storeMapper, times(1)).toDto((StoreEntity) any());
  }
}