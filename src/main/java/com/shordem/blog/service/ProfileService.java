package com.shordem.blog.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shordem.blog.dto.ProfileDto;
import com.shordem.blog.dto.RoleDto;
import com.shordem.blog.dto.UserDto;
import com.shordem.blog.entity.Profile;
import com.shordem.blog.entity.User;
import com.shordem.blog.payload.request.ProfileRequest;
import com.shordem.blog.repository.ProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

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

    public void createProfile(ProfileRequest profileRequest, User user) {

        Profile entity = new Profile();

        entity.setFirstName(profileRequest.getFirstName());
        entity.setLastName(profileRequest.getLastName());
        entity.setBio(profileRequest.getBio());
        entity.setAvatar(profileRequest.getAvatar());
        entity.setWebsite(profileRequest.getWebsite());
        entity.setMail(profileRequest.getMail());
        entity.setX(profileRequest.getX());
        entity.setFacebook(profileRequest.getFacebook());
        entity.setInstagram(profileRequest.getInstagram());
        entity.setCreatedBy(user);

        profileRepository.save(entity);
    }

    public void updateProfile(Profile profile, ProfileRequest profileRequest) {

        profile.setFirstName(profileRequest.getFirstName());
        profile.setLastName(profileRequest.getLastName());
        profile.setBio(profileRequest.getBio());
        profile.setAvatar(profileRequest.getAvatar());
        profile.setWebsite(profileRequest.getWebsite());
        profile.setMail(profileRequest.getMail());
        profile.setX(profileRequest.getX());
        profile.setFacebook(profileRequest.getFacebook());
        profile.setInstagram(profileRequest.getInstagram());
        profile.setUpdatedBy(profile.getCreatedBy());

        profileRepository.save(profile);
    }

    public Boolean existsByUserId(UUID userId) {
        return profileRepository.existsByCreatedById(userId);
    }
}
