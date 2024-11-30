package ru.damrin.app.model.category;

public record CategoryRequest(CategoryDto categoryDto, boolean needRecalculation) {
}
