package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.OrderEntity;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface OrdersRepository extends JpaRepository<OrderEntity, Long> {

  Set<OrderEntity> findAllByCreatedAtBefore(LocalDate createdAt);
}
