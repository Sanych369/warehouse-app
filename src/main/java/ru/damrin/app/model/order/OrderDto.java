package ru.damrin.app.model.order;

import jakarta.validation.constraints.NotNull;
import ru.damrin.app.model.UserDto;
import ru.damrin.app.model.company.CompanyDto;

import java.time.LocalDate;

/**
 * DTO for {@link ru.damrin.app.db.entity.OrderEntity}
 */
public record OrderDto(Long id, @NotNull UserDto user, CompanyDto company,  LocalDate createdAt, Long totalAmount) {
}