package ru.damrin.app.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.CategoryEntity;

import java.math.BigDecimal;

/**
 * Слой репозитория для запросов категорий.
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  @Query(value = "SELECT c FROM CategoryEntity c WHERE " +
      "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
      "(:markupPercentage IS NULL OR c.markupPercentage = :markupPercentage)")
  Page<CategoryEntity> findByFilters(@Param("name") String name,
                                     @Param("markupPercentage") BigDecimal markupPercentage,
                                     Pageable pageable);
}
