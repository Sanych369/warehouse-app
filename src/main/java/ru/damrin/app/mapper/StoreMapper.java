package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.StoreEntity;
import ru.damrin.app.model.StoreDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StoreMapper {
  StoreEntity toEntity(StoreDto storeDto);

  StoreDto toDto(StoreEntity storeEntity);

  List<StoreDto> toDto(List<StoreEntity> storeEntity);

  StoreEntity partialUpdate(StoreDto storeDto, @MappingTarget StoreEntity storeEntity);
}