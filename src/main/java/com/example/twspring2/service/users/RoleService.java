package com.example.twspring2.service.users;

import com.example.twspring2.database.users.model.RoleEntity;
import com.example.twspring2.database.users.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(RoleEntity role) {
        this.roleRepository.save(role);
    }

    public Boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Optional<RoleEntity> findByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public List<RoleEntity> findAll() {
        return this.roleRepository.findAll();
    }

    public void removeRole(Long userId, String roleName) {
        RoleEntity role = this.roleRepository.findByName(roleName).orElseThrow();
        this.roleRepository.removeRole(userId, role.getId());
    }

    public List<RoleEntity> findAllByUserIdNot(Long userId) {
        return this.roleRepository.findAllByUserIdNot(userId);
    }

    public void deleteById(Long id) {
        this.roleRepository.removeRoleFromAll(id);
        this.roleRepository.deleteById(id);
    }

    public void deletePermission(Long permissionId) {
        this.roleRepository.removePermission(permissionId);
    }

    public RoleEntity findById(Long id) {
        return this.roleRepository.findById(id).orElseThrow();
    }
}
