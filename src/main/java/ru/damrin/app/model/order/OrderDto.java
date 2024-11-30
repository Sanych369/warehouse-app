package ru.damrin.app.model.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.damrin.app.model.good.GoodDto;
import ru.damrin.app.model.UserDto;
import ru.damrin.app.model.company.CompanyDto;

import java.util.Set;

/**
 * DTO for {@link ru.damrin.app.db.entity.OrderEntity}
 */
public record OrderDto(Long id, @NotNull UserDto user, CompanyDto company,
                       @NotNull @Size(min = 1) Set<GoodDto> ordersGoods) {
}