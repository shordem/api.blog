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
        this.initRoles();
        this.initAdmin();

        flyway.migrate();
    }

    public void initRoles() {

        for (ERole role : ERole.values()) {
            if (this.roleService.findByName(role).isEmpty()) {
                Role newRole = new Role();
                newRole.setName(role);
                this.roleService.save(newRole);
            }
        }

    }

    public void initAdmin() {

        if (!this.userService.findByUsername("admin").isPresent()) {
            Set<Role> roles = new HashSet<>();

            roles.add(roleService.findByName(ERole.ADMIN)
                    .orElseThrow(() -> new EntityNotFoundException("Admin Role not found")));

            User user = new User();
            user.setEmail("shordem@gmail.com");
            user.setUsername("admin");
            user.setPassword(encoder.encode("password"));
            user.setRoles(roles);

            this.userService.save(user);
        }

    }

}
