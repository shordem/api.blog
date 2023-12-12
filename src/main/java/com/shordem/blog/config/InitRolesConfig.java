package com.shordem.blog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shordem.blog.entity.ERole;
import com.shordem.blog.entity.Role;
import com.shordem.blog.service.RoleService;

@Configuration
public class InitRolesConfig {

    @Bean
    public CommandLineRunner initRoles(RoleService roleService) {
        return args -> {
            for (ERole role : ERole.values()) {
                if (roleService.findByName(role).isEmpty()) {
                    Role newRole = new Role();
                    newRole.setName(role);
                    roleService.saveRole(newRole);
                }
            }
        };
    }
}