package com.shordem.blog.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shordem.blog.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile> findByCreatedById(UUID user);

    Boolean existsByCreatedById(UUID user);
}
