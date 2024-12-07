package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.CategoryEntity;
import ru.damrin.app.model.category.CategoryDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

  CategoryEntity toEntity(CategoryDto categoryEntityDto);

  CategoryDto toDto(CategoryEntity categoryEntity);

  List<CategoryDto> toDto(List<CategoryEntity> categoryEntity);

  void partialUpdate(CategoryDto categoryEntityDto, @MappingTarget CategoryEntity categoryEntity);
}