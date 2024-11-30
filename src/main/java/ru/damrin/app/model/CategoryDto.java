package ru.damrin.app.model;

import java.math.BigDecimal;

/**
 * DTO for {@link ru.damrin.app.db.entity.CategoryEntity}
 */
public record CategoryDto(Long id, String name, BigDecimal markupPercentage) {
}