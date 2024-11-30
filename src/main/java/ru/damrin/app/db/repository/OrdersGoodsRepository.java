package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.OrdersGoodsEntity;

@Repository
public interface OrdersGoodsRepository extends JpaRepository<OrdersGoodsEntity, Long> {
}