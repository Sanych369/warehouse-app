package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.db.entity.CategoryEntity;
import ru.damrin.app.model.CategoryDto;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

  CategoryDto entityToDto(CategoryEntity category);

  CategoryEntity dtoToEntity(CategoryDto categoryDto);

  List<CategoryDto> entityToDtoList(List<CategoryEntity> categoryEntities);
}
