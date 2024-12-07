package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.StoreEntity;

import java.util.List;

/**
 * Слой репозитория для запросов истории складских операций.
 */
@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

  List<StoreEntity> findAllByOrderByCreatedAtDesc();
}