package com.factory.rbtmanager.controller;

import com.factory.rbtmanager.entity.UserEntity;
import com.factory.rbtmanager.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntity createUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        return userManagementService.createUser(username, password, role);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntity> getAllUsers() {
        return userManagementService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntity getUser(@PathVariable Long id) {
        return userManagementService.getUser(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userManagementService.deleteUser(id);
    }
}