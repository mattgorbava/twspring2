package com.example.twspring2.database.users.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "permissions")
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
}
