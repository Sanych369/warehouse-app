package ru.damrin.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.damrin.app.db.entity.CompanyEntity;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.entity.OrderEntity;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.db.repository.CompanyRepository;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.db.repository.OrdersRepository;
import ru.damrin.app.mapper.OrderMapper;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.PrepareUtil.COMPANY_NAME;
import static utils.PrepareUtil.GOOD_BALANCE;
import static utils.PrepareUtil.GOOD_ID;
import static utils.PrepareUtil.ORDER_TOTAL_AMOUNT;
import static utils.PrepareUtil.SALE_PRICE;
import static utils.PrepareUtil.USER_NAME;
import static utils.PrepareUtil.createOrderDto;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Spy
  private SortService sortService;

  @Mock
  private OrderMapper orderMapper;

  @Mock
  private GoodRepository goodRepository;

  @Mock
  private OrdersRepository ordersRepository;

  @Mock
  private CompanyRepository companyRepository;

  @InjectMocks
  private OrderService orderService;

  @Test
  void getPageOrders() {
    when(ordersRepository.findAllWithFilters(any(), any(), any(), any(), any(), any()))
        .thenReturn(mock());

    orderService.getPageOrders(USER_NAME, COMPANY_NAME, ORDER_TOTAL_AMOUNT, null, null, 1, 10, "manager_asc");
    verify(sortService, times(1)).getSortOrderForOrders(any());
    verify(ordersRepository, times(1)).findAllWithFilters(any(), any(), any(), any(), any(), any());
  }

  @Test
  void findAll() {
    when(ordersRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Set.of(new OrderEntity()));

    orderService.findAll();

    verify(ordersRepository, times(1)).findAllByOrderByCreatedAtDesc();
    verify(orderMapper, times(1)).toDto(any());
  }

  @Test
  void createOrder() {
    when(companyRepository.findById(any())).thenReturn(Optional.of(createCompanyEntity()));
    when(goodRepository.findById(any())).thenReturn(Optional.of(createGoodEntity()));

    orderService.createOrder(createOrderDto(), new UserEntity());

    verify(companyRepository, times(1)).findById(any());
    verify(goodRepository, times(1)).findById(any());
    verify(ordersRepository, times(1)).save(any());
  }

  private CompanyEntity createCompanyEntity() {
    return CompanyEntity.builder()
        .isActive(true)
        .build();
  }

  private GoodEntity createGoodEntity() {
    return GoodEntity.builder()
        .id(GOOD_ID)
        .salePrice(SALE_PRICE)
        .balance(GOOD_BALANCE)
        .build();
  }
}