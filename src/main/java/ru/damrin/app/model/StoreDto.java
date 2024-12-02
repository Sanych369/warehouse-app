package ru.damrin.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link ru.damrin.app.db.entity.StoreEntity}
 */
public record StoreDto(String responsiblePerson, String goodName, BigDecimal purchasePrice, Long arrivedTotal,
                       Long consumptionTotal, String reason, LocalDateTime createdAt) {
}