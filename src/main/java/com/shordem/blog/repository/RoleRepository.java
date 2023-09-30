package com.shordem.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shordem.blog.entity.ERole;
import com.shordem.blog.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(ERole name);
}
