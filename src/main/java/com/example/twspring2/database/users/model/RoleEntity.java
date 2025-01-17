package com.example.twspring2.database.users.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "role")
@Transactional
public class RoleEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UserEntity> users = new ArrayList<UserEntity>();

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PermissionEntity> permissions = new ArrayList<>();

    public void setRole(Role role) {
        this.name= role.name();
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }

    public void addPermission(PermissionEntity permission) {
        permissions.add(permission);
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
