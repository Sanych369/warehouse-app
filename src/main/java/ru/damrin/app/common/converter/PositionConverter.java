package ru.damrin.app.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.damrin.app.common.enums.GoodCategory;
import ru.damrin.app.common.enums.Position;

import java.util.Arrays;

@Converter
public class PositionConverter implements AttributeConverter<Position, String> {

  @Override
  public String convertToDatabaseColumn(Position attribute) {
    return attribute.getPositionName();
  }

  @Override
  public Position convertToEntityAttribute(String dbData) {
    return Arrays.stream(Position.values())
        .filter(x -> x.getPositionName().equals(dbData))
        .findFirst()
        .orElse(null);
  }
}
