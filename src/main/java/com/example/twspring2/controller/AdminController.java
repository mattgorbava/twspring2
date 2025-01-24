package com.example.twspring2.controller;

import com.example.twspring2.database.users.model.PermissionEntity;
import com.example.twspring2.database.users.model.RoleEntity;
import com.example.twspring2.database.users.model.UserEntity;
import com.example.twspring2.service.users.PermissionService;
import com.example.twspring2.service.users.RoleService;
import com.example.twspring2.service.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("welcomeMessage", "Hello admin");
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/roles")
    public String getRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/roles";
    }

    @PostMapping("/roles")
    public String postRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "admin/roles";
    }

    @GetMapping("/users/{id}/roles")
    public String getUserRoles(@PathVariable("id")Long id, Model model) {
        UserEntity user = userService.findById(id);
        List<RoleEntity> roles = roleService.findAllByUserIdNot(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "admin/user_roles";
    }

    @PostMapping("/users/{id}/roles")
    public String postUserRoles(@PathVariable("id")Long id, Model model) {
        UserEntity user = userService.findById(id);
        List<RoleEntity> roles = roleService.findAllByUserIdNot(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "admin/user_roles";
    }

    @PostMapping("/remove-role")
    public String removeRole(@ModelAttribute("user-id")long userId, @ModelAttribute("role-name")String roleName, Model model) {
        userService.removeRole(userId, roleName);
        model.addAttribute("id", userId);
        return "redirect:/admin/users/" + userId + "/roles";
    }

    @PostMapping("/add-role")
    public String addRole(@ModelAttribute("user-id")long userId, @ModelAttribute("role-name")String roleName, Model model) {
        userService.addRole(userId, roleName);
        model.addAttribute("id", userId);
        return "redirect:/admin/users/" + userId + "/roles";
    }

    @PostMapping("roles/{id}/delete")
    public String deleteRole(@PathVariable("id")Long id, Model model) {
        roleService.deleteById(id);
        return "redirect:/admin/roles";
    }

    @PostMapping("/permissions/{permissionId}/delete")
    public String deletePermission(@PathVariable("permissionId")Long permissionId, Model model) {
        roleService.deletePermission(permissionId);
        return "redirect:/admin/roles";
    }

    @PostMapping("/roles/{id}/new-name")
    public String editRole(@PathVariable("id")Long id, @ModelAttribute("role-name")String roleName) {
        RoleEntity role = roleService.findById(id);
        role.setName(roleName);
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @PostMapping("/roles/{id}/new-permission")
    public String addPermission(@PathVariable("id")Long id, @ModelAttribute("url")String url, @ModelAttribute("method")String method) {
        PermissionEntity permission = new PermissionEntity();
        permission.setUrl(url);
        permission.setHttpMethod(method);
        permission.setRole(roleService.findById(id));
        permissionService.save(permission);
        RoleEntity role = roleService.findById(id);
        role.addPermission(permission);
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @PostMapping("/roles/new")
    public String newRole(@ModelAttribute("role-name")String roleName) {
        RoleEntity role = new RoleEntity();
        role.setName(roleName);
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/permissions/edit/{permissionId}")
    public String editPermission(@PathVariable("permissionId")Long permissionId, Model model) {
        PermissionEntity permission = permissionService.findById(permissionId);
        model.addAttribute("permission", permission);
        return "admin/edit_permission";
    }

    @PostMapping("/permissions/{id}/update")
    public String updatePermission(@PathVariable("id")Long id, @ModelAttribute("url")String url, @ModelAttribute("method")String method) {
        PermissionEntity permission = permissionService.findById(id);
        permission.setUrl(url);
        permission.setHttpMethod(method);
        permissionService.save(permission);
        return "redirect:/admin/roles";
    }
}
