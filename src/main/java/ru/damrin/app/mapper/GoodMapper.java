package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.entity.OrdersGoodsEntity;
import ru.damrin.app.model.good.GoodDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {CategoryMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GoodMapper {

  GoodEntity toEntity(GoodDto goodDto);

  GoodDto toDto(GoodEntity goodEntity);

  GoodEntity partialUpdate(GoodDto goodDto, @MappingTarget GoodEntity goodEntity);

  OrdersGoodsEntity toEntity1(GoodDto goodDto);

  GoodDto toDto(OrdersGoodsEntity ordersGoodsEntity);

  OrdersGoodsEntity partialUpdate(GoodDto goodDto, @MappingTarget OrdersGoodsEntity ordersGoodsEntity);
}