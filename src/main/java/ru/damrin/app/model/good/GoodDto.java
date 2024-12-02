package ru.damrin.app.model.good;

import ru.damrin.app.model.category.CategoryDto;

import java.math.BigDecimal;

/**
 * DTO for {@link ru.damrin.app.db.entity.GoodEntity}
 */
public record GoodDto(Long id, String name, CategoryDto category, BigDecimal purchasePrice, Long salePrice, Long balance) {
}