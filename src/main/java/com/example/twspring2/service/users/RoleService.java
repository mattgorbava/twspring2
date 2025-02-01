package com.example.twspring2.service.users;

import com.example.twspring2.database.users.model.PermissionEntity;
import com.example.twspring2.database.users.model.RoleEntity;
import com.example.twspring2.database.users.model.UserEntity;
import com.example.twspring2.database.users.repository.RoleRepository;
import com.example.twspring2.database.users.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(RoleService.class);
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PermissionService permissionService;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.permissionService = permissionService;
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

    public void createAlbumRoles(String albumTitle, String username) {
        RoleEntity role = new RoleEntity();
        role.setName(albumTitle.toUpperCase() + "_OWNER");
        this.save(role);
        permissionService.createAlbumOwnerRolePermissions(role, albumTitle);
        RoleEntity userRole = new RoleEntity();
        userRole.setName(albumTitle.toUpperCase() + "_USER");
        logger.info("Creating role: " + userRole.getName());
        this.save(userRole);
        permissionService.createAlbumUserPermissions(userRole, albumTitle);

        UserEntity user = null;
        try{
            user = this.userRepository.findByUsername(username).orElseThrow();
        } catch (Exception e) {
            try {
                user = this.userRepository.findByEmail(username).orElseThrow();
            } catch (Exception e2) {
                logger.error("User not found: " + username);
            }
        }
        assert user != null;
        user.addRole(role);
    }

    public void addRole(UserEntity user, String albumTitle) {
        RoleEntity role = this.roleRepository.findByName(albumTitle.toUpperCase()).orElseThrow();
        user.addRole(role);
        this.userRepository.save(user);
    }
}
