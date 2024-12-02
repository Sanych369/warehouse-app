package ru.damrin.app.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.damrin.app.db.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  @Query("SELECT u FROM UserEntity u WHERE " +
      "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
      "(:surname IS NULL OR LOWER(u.surname) LIKE LOWER(CONCAT('%', :surname, '%'))) AND " +
      "(:position IS NULL OR LOWER(u.position) LIKE LOWER(CONCAT('%', :position, '%'))) AND " +
      "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
  Page<UserEntity> findByFilters(@Param("name") String name,
                                 @Param("surname") String surname,
                                 @Param("position") String position,
                                 @Param("email") String email,
                                 Pageable pageable);
}
