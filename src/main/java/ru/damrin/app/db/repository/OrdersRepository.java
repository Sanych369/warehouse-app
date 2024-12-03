package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.OrderEntity;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface OrdersRepository extends JpaRepository<OrderEntity, Long> {

  Set<OrderEntity> findAllByCreatedAtBetween(LocalDate from, LocalDate to);
  Set<OrderEntity> findAllByUserIdAndCreatedAtBetween(Long userId, LocalDate from, LocalDate to);

  Set<OrderEntity> findAllByUserIdAndCreatedAt(Long userId, LocalDate createdAt);

  Set<OrderEntity> findAllByOrderByCreatedAtDesc();

  Set<OrderEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
