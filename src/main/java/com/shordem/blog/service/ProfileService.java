package com.shordem.blog.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shordem.blog.entity.Profile;
import com.shordem.blog.repository.ProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService extends BaseService<Profile> {

    private final ProfileRepository profileRepository;

    public Optional<Profile> findByUserId(UUID userId) {
        return profileRepository.findByUserId(userId);
    }

    @Override
    public void save(Profile entity) {
        profileRepository.save(entity);
    }

    public Boolean existsByUserId(UUID userId) {
        return profileRepository.existsByUserId(userId);
    }
}
