package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.common.enums.GoodCategory;
import ru.damrin.app.db.entity.CategoryEntity;

import java.util.Optional;

@Repository
public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Long> {
  Optional<CategoryEntity> findByCategory(GoodCategory category);
}