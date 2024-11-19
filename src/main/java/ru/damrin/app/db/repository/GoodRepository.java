package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.GoodEntity;

@Repository
public interface GoodRepository extends JpaRepository<GoodEntity, Long> {
}