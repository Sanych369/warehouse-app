package ru.damrin.app.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.GoodEntity;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Слой репозитория для запросов товаров.
 */
@Repository
public interface GoodRepository extends JpaRepository<GoodEntity, Long> {

  Optional<GoodEntity> findByName(String name);

  @Query("SELECT g FROM GoodEntity g WHERE " +
      "(:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
      "(:categoryId IS NULL OR g.category.id = :categoryId)")
  Page<GoodEntity> findByFiltersForOrder(
      @Param("name") String name,
      @Param("categoryId") Long categoryId,
      Pageable pageable);

  @Query("SELECT g FROM GoodEntity g WHERE " +
      "(:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
      "(:category IS NULL OR LOWER(g.category.name) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
      "(:purchasePrice IS NULL OR g.purchasePrice = :purchasePrice) AND " +
      "(:salePrice IS NULL OR g.salePrice = :salePrice) AND " +
      "(:balance IS NULL OR g.balance = :balance)")
  Page<GoodEntity> findByFilters(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("purchasePrice") BigDecimal purchasePrice,
                                 @Param("salePrice") Long salePrice,
                                 @Param("balance") Long balance,
                                 Pageable pageable);
}