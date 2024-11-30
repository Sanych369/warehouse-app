package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.GoodEntity;

import java.util.List;

@Repository
public interface GoodRepository extends JpaRepository<GoodEntity, Long> {

    List<GoodEntity> findAllByCategoryId(@NonNull Long categoryId);

}