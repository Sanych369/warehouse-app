package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.OrdersGoodsEntity;
import ru.damrin.app.model.OrdersGoodsDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdersGoodsMapper {
  OrdersGoodsEntity toEntity(OrdersGoodsDto ordersGoodsDto);

  OrdersGoodsDto toDto(OrdersGoodsEntity ordersGoodsEntity);

  OrdersGoodsEntity partialUpdate(OrdersGoodsDto ordersGoodsDto, @MappingTarget OrdersGoodsEntity ordersGoodsEntity);
}