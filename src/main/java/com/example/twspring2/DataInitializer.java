package com.example.twspring2;

import com.example.twspring2.database.users.model.PermissionEntity;
import com.example.twspring2.database.users.model.Role;
import com.example.twspring2.database.users.model.RoleEntity;
import com.example.twspring2.database.users.model.UserEntity;
import com.example.twspring2.service.users.PermissionService;
import com.example.twspring2.service.users.RoleService;
import com.example.twspring2.service.users.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {
    private final List<String> CRUD_METHODS = List.of("POST", "GET", "PATCH", "DELETE");
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public DataInitializer(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        /* ROLES */
        RoleEntity admin = new RoleEntity();
        admin.setRole(Role.ADMIN);

        RoleEntity user = new RoleEntity();
        user.setRole(Role.USER);

        RoleEntity defaultRole = new RoleEntity();
        defaultRole.setRole(Role.DEFAULT);

        Boolean isRoleAdminInDb = roleService.existsByName(admin.getName());
        if(!isRoleAdminInDb) {
            roleService.save(admin);
            for (String crudMethod : CRUD_METHODS) {
                PermissionEntity permissionEntity = new PermissionEntity();
                permissionEntity.setHttpMethod(crudMethod);
                permissionEntity.setUrl("/**");
                permissionEntity.setRole(admin);
                permissionService.save(permissionEntity);
            }
        }

        Boolean isRoleUserInDb = roleService.existsByName(user.getName());
        if(!isRoleUserInDb) {
            roleService.save(user);
            PermissionEntity getHome = new PermissionEntity();
            getHome.setHttpMethod("GET");
            getHome.setUrl("/home");
            getHome.setRole(user);
            permissionService.save(getHome);
            PermissionEntity getCreateAlbum = new PermissionEntity();
            getCreateAlbum.setHttpMethod("GET");
            getCreateAlbum.setUrl("/home/albums/create");
            getCreateAlbum.setRole(user);
            permissionService.save(getCreateAlbum);
            PermissionEntity postCreateAlbum = new PermissionEntity();
            postCreateAlbum.setHttpMethod("POST");
            postCreateAlbum.setUrl("/home/albums/create");
            postCreateAlbum.setRole(user);
            permissionService.save(postCreateAlbum);
            PermissionEntity getHomeSearch = new PermissionEntity();
            getHomeSearch.setHttpMethod("GET");
            getHomeSearch.setUrl("/home/search/**");
            getHomeSearch.setRole(user);
            permissionService.save(getHomeSearch);
        }

        Boolean isRoleDefaultInDb = roleService.existsByName(defaultRole.getName());
        if(!isRoleDefaultInDb)
            roleService.save(defaultRole);

        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("Administrator");
        adminUser.setEmail("admin@admin.com");
        adminUser.setPassword("admin");
        adminUser.addRole(admin);

        Boolean isUserPresentInDatabase = this.userService.existsByEmail(adminUser.getEmail());

        if(!isUserPresentInDatabase)
            this.userService.save(adminUser);
    }
}
