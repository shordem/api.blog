package com.shordem.blog.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shordem.blog.entity.ERole;
import com.shordem.blog.entity.Role;
import com.shordem.blog.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService extends BaseService<Role> {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void save(Role entity) {
        roleRepository.save(entity);
    }
}
