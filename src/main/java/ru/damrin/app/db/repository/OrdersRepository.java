package ru.damrin.app.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.OrderEntity;

import java.time.LocalDate;
import java.util.Set;

/**
 * Слой репозитория для запросов заказов.
 */
@Repository
public interface OrdersRepository extends JpaRepository<OrderEntity, Long> {

  @Query("SELECT o FROM OrderEntity o WHERE " +
      "(:username IS NULL OR LOWER(o.user.name) LIKE LOWER(CONCAT('%', :username, '%')) OR " +
      " :username IS NULL OR LOWER(o.user.surname) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
      "(:companyName IS NULL OR LOWER(o.company.name) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
      "(:totalAmount IS NULL OR o.totalAmount = :totalAmount) AND " +
      "(CAST(:dateFrom AS DATE ) IS NULL OR o.createdAt >= :dateFrom) AND " +
      "(CAST(:dateTo AS DATE )IS NULL OR o.createdAt <= :dateTo)")
  Page<OrderEntity> findAllWithFilters(String username,
                                       String companyName,
                                       Long totalAmount,
                                       LocalDate dateFrom,
                                       LocalDate dateTo,
                                       Pageable pageable);

  @Query("SELECT o FROM OrderEntity o WHERE " +
      "o.user.id = :userId AND (:company IS NULL OR o.company.name LIKE %:company%) AND " +
      "(:totalAmount IS NULL OR o.totalAmount = :totalAmount) AND " +
      "(CAST(:dateFrom AS DATE ) IS NULL OR o.createdAt >= :dateFrom) AND " +
      "(CAST(:dateTo AS DATE )IS NULL OR o.createdAt <= :dateTo)")
  Page<OrderEntity> findAllWithFiltersForUser(
      @Param("userId") Long userId,
      @Param("company") String company,
      @Param("totalAmount") Long totalAmount,
      @Param("dateFrom") LocalDate dateFrom,
      @Param("dateTo") LocalDate dateTo,
      Pageable pageable);

  Set<OrderEntity> findAllByCreatedAtBetween(LocalDate from, LocalDate to);

  Set<OrderEntity> findAllByUserIdAndCreatedAtBetween(Long userId, LocalDate from, LocalDate to);

  Set<OrderEntity> findAllByOrderByCreatedAtDesc();

}
