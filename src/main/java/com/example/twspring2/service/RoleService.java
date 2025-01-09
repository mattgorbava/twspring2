package com.example.twspring2.service;

import com.example.twspring2.database.model.RoleEntity;
import com.example.twspring2.database.repository.RoleRepository;
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
}
