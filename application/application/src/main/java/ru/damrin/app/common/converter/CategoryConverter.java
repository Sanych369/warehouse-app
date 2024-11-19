package ru.damrin.app.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.damrin.app.common.enums.GoodCategory;

@Converter
public class CategoryConverter implements AttributeConverter<GoodCategory, String> {

  @Override
  public String convertToDatabaseColumn(GoodCategory attribute) {
    return attribute.getCategoryName();
  }

  @Override
  public GoodCategory convertToEntityAttribute(String dbData) {
    return GoodCategory.valueOf(dbData);
  }
}
