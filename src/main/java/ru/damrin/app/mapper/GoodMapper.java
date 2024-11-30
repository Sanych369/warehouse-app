package ru.damrin.app.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.model.GoodDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class})
public interface GoodMapper {
  GoodEntity toEntity(GoodDto goodDto);

  GoodDto toDto(GoodEntity goodEntity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  GoodEntity partialUpdate(GoodDto goodDto, @MappingTarget GoodEntity goodEntity);
}