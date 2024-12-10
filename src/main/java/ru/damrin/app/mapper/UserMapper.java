package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.damrin.app.common.enums.Position;
import ru.damrin.app.db.entity.UserEntity;
import ru.damrin.app.model.UserDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  @Mapping(target = "password", ignore = true)
  UserEntity toEntity(UserDto userDto);

  @Mapping(target = "password", ignore = true)
  UserDto toDto(UserEntity userEntity);

  @Mapping(target = "password", ignore = true)
  UserEntity partialUpdate(UserDto userDto, @MappingTarget UserEntity userEntity);

  default String map(Position position) {
    return position.getPositionName();
  }
}
