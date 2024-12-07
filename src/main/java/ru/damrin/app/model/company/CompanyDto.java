package ru.damrin.app.model.company;

import ru.damrin.app.db.entity.CompanyEntity;

/**
 * DTO for {@link CompanyEntity}
 */

public record CompanyDto(Long id, String name, String address, String phone, String email, Boolean isActive) {
}