package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.WarehouseUserEntity;

import java.util.Optional;

@Repository
public interface WarehouseUserRepository extends JpaRepository<WarehouseUserEntity, Long> {

  Optional<WarehouseUserEntity> findByEmail(String email);
}
