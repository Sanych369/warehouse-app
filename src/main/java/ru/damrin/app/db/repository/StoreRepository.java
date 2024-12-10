package ru.damrin.app.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.StoreEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Слой репозитория для запросов истории складских операций.
 */
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

  List<StoreEntity> findAllByOrderByCreatedAtDesc();

  @Query("SELECT s FROM StoreEntity s WHERE " +
      "(:goodName IS NULL OR LOWER(s.good.name) LIKE LOWER(CONCAT('%', :goodName, '%'))) AND " +
      "(:purchasePrice IS NULL OR s.purchasePrice = :purchasePrice) AND " +
      "(:arrivedTotal IS NULL OR s.arrivedTotal = :arrivedTotal) AND " +
      "(:consumptionTotal IS NULL OR s.consumptionTotal = :consumptionTotal) AND " +
      "(:reason IS NULL OR LOWER(s.reason) LIKE LOWER(CONCAT('%', :reason, '%'))) AND " +
      "(CAST(:dateFrom AS DATE) IS NULL OR s.createdAt >= :dateFrom) AND " +
      "(CAST(:dateTo AS DATE) IS NULL OR s.createdAt <= :dateTo)")
  Page<StoreEntity> findAllWithFilters(
      @Param("goodName") String goodName,
      @Param("purchasePrice") BigDecimal purchasePrice,
      @Param("arrivedTotal") Long arrivedTotal,
      @Param("consumptionTotal") Long consumptionTotal,
      @Param("reason") String reason,
      @Param("dateFrom") LocalDateTime dateFrom,
      @Param("dateTo") LocalDateTime dateTo,
      Pageable pageable);
}