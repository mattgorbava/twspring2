package com.example.twspring2.database.users.repository;

import com.example.twspring2.database.users.model.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findAllByName(String roleName);
    Boolean existsByName(String name);

    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from app_users_roles where role_id = ?1", nativeQuery = true)
    void removeRoleFromAll(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from permissions where id = ?1", nativeQuery = true)
    void removePermission(Long permissionId);

    Optional<RoleEntity> findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "delete from app_users_roles where app_user_id = ?1 and role_id = ?2", nativeQuery = true)
    void removeRole(Long userId, Long roleId);

    @Transactional
    @Query(value = "select r.* from role r left join app_users_roles aur on r.id = aur.role_id and aur.app_user_id = ?1 where aur.app_user_id is null", nativeQuery = true)
    List<RoleEntity> findAllByUserIdNot(Long userId);
}
