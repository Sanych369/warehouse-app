package ru.damrin.app.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.damrin.app.common.enums.GoodCategory;

import java.util.Arrays;

@Converter
public class CategoryConverter implements AttributeConverter<GoodCategory, String> {

  @Override
  public String convertToDatabaseColumn(GoodCategory attribute) {
    return attribute.getCategoryName();
  }

  @Override
  public GoodCategory convertToEntityAttribute(String dbData) {
    return Arrays.stream(GoodCategory.values())
        .filter(x -> x.getCategoryName().equals(dbData))
        .findFirst()
        .orElse(null);
  }
}
