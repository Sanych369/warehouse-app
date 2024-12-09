package ru.damrin.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO получения информации о складе.
 */
public record StoreDto(String goodName, BigDecimal purchasePrice, Long arrivedTotal, Long consumptionTotal,
                       String reason, LocalDateTime createdAt) {
}