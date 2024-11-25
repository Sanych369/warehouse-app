package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.damrin.app.db.entity.CategoryEntity;
import ru.damrin.app.db.repository.CategoryEntityRepository;
import ru.damrin.app.model.CategoryDto;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryEntityRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getName()))
                .toList();
    }


    public void addCategory(CategoryDto categoryDto) {
        CategoryEntity category = CategoryEntity.builder()
                .name(categoryDto.name())
                .build();
        categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}