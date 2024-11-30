package ru.damrin.app.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.CompanyEntity;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

  List<CompanyEntity> findAllByActiveIsTrue();
  List<CompanyEntity> findAllByActiveIsFalse();
}