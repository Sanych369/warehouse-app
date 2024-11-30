package ru.damrin.app.model;

import ru.damrin.app.db.entity.CompanyEntity;

/**
 * DTO for {@link CompanyEntity}
 */
public record CompanyDto(String name, String address, String phone, String email) {
}