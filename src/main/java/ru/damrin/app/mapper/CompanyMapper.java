package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.model.company.CompanyDto;
import ru.damrin.app.db.entity.CompanyEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompanyMapper {

  CompanyEntity toEntity(CompanyDto companyDto);

  CompanyDto toDto(CompanyEntity companyEntity);

  void partialUpdate(CompanyDto companyDto, @MappingTarget CompanyEntity companyEntity);
}