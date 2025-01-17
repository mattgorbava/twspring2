package com.example.twspring2.database.users.repository;

import com.example.twspring2.database.users.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
