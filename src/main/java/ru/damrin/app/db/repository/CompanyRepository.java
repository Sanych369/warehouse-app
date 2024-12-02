package ru.damrin.app.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.CompanyEntity;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

  List<CompanyEntity> findAllByIsActiveIsTrue();

  List<CompanyEntity> findAllByIsActiveIsFalse();

  @Query("SELECT c FROM CompanyEntity c WHERE " +
      "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
      "(:address IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
      "(:phone IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%'))) AND " +
      "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
      "(COALESCE(:isActive, NULL) IS NULL OR c.isActive = :isActive)")
  Page<CompanyEntity> findByFilters(@Param("name") String name,
                                    @Param("address") String address,
                                    @Param("phone") String phone,
                                    @Param("email") String email,
                                    @Param("isActive") Boolean isActive,
                                    Pageable pageable);
}