package com.example.twspring2.service;

import com.example.twspring2.database.model.PermissionEntity;
import com.example.twspring2.database.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
