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
import com.shordem.blog.service.UserService;

@Component
public class DatabaseMigrationRunner implements CommandLineRunner {

    @Autowired
    Flyway flyway;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userService.findByUsername("admin").isPresent()) {
            Set<Role> roles = new HashSet<>();

            roles.add(new Role(ERole.ADMIN));

            User user = new User();
            user.setEmail("shordemmedia@gmail.com");
            user.setUsername("admin");
            user.setPassword(encoder.encode("password"));
            user.setRoles(roles);

            userService.save(user);
        }
        flyway.migrate();
    }

}
