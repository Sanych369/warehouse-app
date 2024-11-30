package ru.damrin.app.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.CategoryEntity;

import java.math.BigDecimal;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);
    Page<CategoryEntity> findByMarkupPercentage(BigDecimal markupPercentage, Pageable pageable);
    Page<CategoryEntity> findByNameContainingAndMarkupPercentage(String name, BigDecimal markupPercentage, Pageable pageable);


}
