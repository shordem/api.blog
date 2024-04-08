package com.shordem.blog.config;

import java.util.HashSet;
import java.util.Set;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.shordem.blog.entity.ERole;
import com.shordem.blog.entity.Role;
import com.shordem.blog.entity.User;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.service.RoleService;
import com.shordem.blog.service.UserService;

@Component
public class DatabaseMigrationRunner implements CommandLineRunner {

    @Autowired
    Flyway flyway;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {

        try {
            flyway.migrate();

            this.initRoles();
            this.initializeAdminUser();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void initRoles() {

        try {
            for (ERole role : ERole.values()) {
                this.saveRoleIfNotExists(role);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private void initializeAdminUser() {

        String adminUsername = "admin";
        String adminEmail = "shordem@email.com";
        String adminPassword = "password";

        if (userExists(adminUsername) == false) {

            Set<Role> roles = new HashSet<>();

            Role adminRole = getAdminRole();

            roles.add(adminRole);

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(encodePassword(adminPassword));
            admin.setIsEmailVerified(true);
            // admin.setRoles(roles);

            // userService.save(admin, roles);

        }

    }

    private boolean userExists(String username) {
        return this.userService.existsByUsername(username);
    }

    private Role getAdminRole() {

        return roleService.findByName(ERole.ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Admin role not found"));

    }

    private String encodePassword(String password) {
        return encoder.encode(password);
    }

    private void saveRoleIfNotExists(ERole role) {

        if (roleService.findByName(role).isEmpty()) {

            Role newRole = new Role();
            newRole.setName(role);

            roleService.save(newRole);

        }
    }

}