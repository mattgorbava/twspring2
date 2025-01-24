package com.example.twspring2.service.users;

import com.example.twspring2.database.albums.model.AlbumEntity;
import com.example.twspring2.database.users.model.PermissionEntity;
import com.example.twspring2.database.users.model.RoleEntity;
import com.example.twspring2.database.users.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void save(PermissionEntity permission) {
        this.permissionRepository.save(permission);
    }

    public void createAlbumOwnerRolePermissions(RoleEntity role, String album) {
        PermissionEntity get = new PermissionEntity();
        get.setUrl("/home/album/" + album + "/**");
        get.setRole(role);
        get.setHttpMethod("GET");
        this.save(get);
        PermissionEntity post = new PermissionEntity();
        post.setUrl("/home/album/" + album + "/**");
        post.setRole(role);
        post.setHttpMethod("POST");
        this.save(post);
        PermissionEntity patch = new PermissionEntity();
        patch.setUrl("/home/album/" + album + "/**");
        patch.setRole(role);
        patch.setHttpMethod("PATCH");
        this.save(patch);
        PermissionEntity delete = new PermissionEntity();
        delete.setUrl("/home/album/" + album + "/**");
        delete.setRole(role);
        delete.setHttpMethod("DELETE");
        this.save(delete);
    }

    public void createAlbumUserPermissions(RoleEntity role, String album) {
        PermissionEntity get = new PermissionEntity();
        get.setUrl("/home/album/" + album + "/**");
        get.setRole(role);
        get.setHttpMethod("GET");
        this.save(get);
    }

    public PermissionEntity findById(Long permissionId) {
        return this.permissionRepository.findById(permissionId).orElse(null);
    }
}
