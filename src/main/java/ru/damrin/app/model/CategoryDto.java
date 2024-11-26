package ru.damrin.app.model;

import ru.damrin.app.common.enums.GoodCategory;

import java.math.BigDecimal;


public record CategoryDto(Long id, String name, BigDecimal markupPercentage) {
  public CategoryDto(String name, BigDecimal markupPercentage) {
    this(null, name, markupPercentage);
  }
  public CategoryDto(String name) {
    this(name, BigDecimal.ZERO);
  }
}