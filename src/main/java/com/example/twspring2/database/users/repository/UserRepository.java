package com.example.twspring2.database.users.repository;

import com.example.twspring2.database.users.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "select au.* from app_user au " +
            "inner join app_users_roles aur on aur.app_user_id = au.id " +
            "inner join role r on aur.role_id = r.id " +
            "where r.name != 'ADMIN' " +
            "and r.name != ?1 " +
            "and r.name != ?2", nativeQuery = true)
    List<UserEntity> findAllWithoutPermission(String userRole, String ownerRole);
}
