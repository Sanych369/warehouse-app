package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.CategoryEntity;
import ru.damrin.app.model.category.CategoryDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

  @Mapping(target = "id", ignore = true)
  CategoryEntity toEntity(CategoryDto categoryEntityDto);

  CategoryDto toDto(CategoryEntity categoryEntity);

  CategoryEntity partialUpdate(CategoryDto categoryEntityDto, @MappingTarget CategoryEntity categoryEntity);
}