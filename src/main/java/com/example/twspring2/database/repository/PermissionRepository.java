package com.example.twspring2.database.repository;

import com.example.twspring2.database.model.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
