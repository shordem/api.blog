package com.shordem.blog.repository;

import java.util.Optional;
import java.util.UUID;

import com.shordem.blog.entity.User;

public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findById(UUID userId);
}