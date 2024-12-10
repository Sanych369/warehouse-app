package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.StoreEntity;
import ru.damrin.app.model.StoreDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StoreMapper {

  @Mapping(target = "arrivedTotal", defaultValue = "0L")
  @Mapping(target = "consumptionTotal", defaultValue = "0L")
  StoreEntity toEntity(StoreDto storeDto);

  @Mapping(target = "goodName", source = "good.name")
  StoreDto toDto(StoreEntity storeEntity);

  List<StoreDto> toDto(List<StoreEntity> storeEntity);
}