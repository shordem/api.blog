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

    private ProfileDto convertToDto(Profile profile) {
        User user = profile.getCreatedBy();
        Set<RoleDto> roles = user.getRoles().stream().map(role -> new RoleDto(role.getName()))
                .collect(Collectors.toSet());

        UserDto createdBy = new UserDto(profile.getCreatedBy().getUsername(), profile.getCreatedBy().getEmail(), roles);

        return new ProfileDto(profile.getFirstName(), profile.getLastName(), profile.getBio(), profile.getAvatar(),
                profile.getWebsite(), profile.getMail(), profile.getX(), profile.getFacebook(), profile.getInstagram(),
                createdBy);
    }

    public Optional<ProfileDto> findByUserId(UUID userId) {
        return profileRepository.findByCreatedById(userId).map(this::convertToDto);
    }

    public Optional<Profile> findByUserIdEntity(UUID userId) {
        return profileRepository.findByCreatedById(userId);
    }

    @Override
    public void save(Profile entity) {
        profileRepository.save(entity);
    }

    public Boolean existsByUserId(UUID userId) {
        return profileRepository.existsByUserId(userId);
    }
}
