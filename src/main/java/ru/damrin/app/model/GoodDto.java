package ru.damrin.app.model;

import java.math.BigDecimal;

/**
 * DTO for {@link ru.damrin.app.db.entity.GoodEntity}
 */
public record GoodDto(Long id, String name, BigDecimal price, Long quantity) {
}