package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.OrdersGoodsEntity;

import java.util.List;

/**
 * Слой репозитория для запросов товаров в заказе.
 */
@Repository
public interface OrdersGoodsRepository extends JpaRepository<OrdersGoodsEntity, Long> {

  List<OrdersGoodsEntity> findAllByOrderId(Long orderId);
}