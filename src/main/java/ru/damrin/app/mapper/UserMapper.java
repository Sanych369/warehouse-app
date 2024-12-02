package ru.damrin.app.mapper;

import org.mapstruct.Mapper;
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

  UserEntity toEntity(UserDto userDto);

  UserDto toDto(UserEntity userEntity);

  UserEntity partialUpdate(UserDto userDto, @MappingTarget UserEntity userEntity);

  default String map(Position position) {
    return position.getPositionName();
  }
}
