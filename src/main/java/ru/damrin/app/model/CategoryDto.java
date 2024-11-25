package ru.damrin.app.model;

import ru.damrin.app.common.enums.GoodCategory;


public record CategoryDto(Long id, GoodCategory category) {
}