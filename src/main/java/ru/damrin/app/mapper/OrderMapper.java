package ru.damrin.app.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.OrderEntity;
import ru.damrin.app.model.order.OrderDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {CompanyMapper.class, GoodMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

  OrderEntity toEntity(OrderDto orderDto);

  @AfterMapping
  default void linkOrdersGoods(@MappingTarget OrderEntity orderEntity) {
    orderEntity.getOrdersGoods().forEach(ordersGood -> ordersGood.setOrder(orderEntity));
  }

  OrderDto toDto(OrderEntity orderEntity);

  OrderEntity partialUpdate(OrderDto orderDto, @MappingTarget OrderEntity orderEntity);
}