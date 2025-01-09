package com.example.twspring2.database.repository;

import com.example.twspring2.database.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    public List<RoleEntity> findAllByName(String roleName);
    Boolean existsByName(String name);

    Optional<RoleEntity> findByName(String name);
}
