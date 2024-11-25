package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {

  Set<Order> findAllByCreatedAtBefore(LocalDate createdAt);
}
