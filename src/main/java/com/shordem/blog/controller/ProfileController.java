package com.shordem.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shordem.blog.dto.ProfileDto;
import com.shordem.blog.entity.Profile;
import com.shordem.blog.entity.User;
import com.shordem.blog.payload.request.ProfileRequest;
import com.shordem.blog.payload.response.MessageResponse;
import com.shordem.blog.service.ProfileService;
import com.shordem.blog.service.UserDetailsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile/")
public class ProfileController {

    private final ProfileService profileService;
    private final UserDetailsService userDetailsService;

    @GetMapping
    public ResponseEntity<?> getProfile() {
        User user = userDetailsService.getAuthenticatedUser();

        ProfileDto profile = profileService.findByUserId(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Profile Not Found with user id: " + user.getId()));

        return ResponseEntity.ok().body(profile);
    }

    @PostMapping
    public ResponseEntity<?> doCreateProfile(@Valid @RequestBody ProfileRequest profileRequest) {

        User user = userDetailsService.getAuthenticatedUser();
        Boolean profileCheck = profileService.existsByUserId(user.getId());

        if (profileCheck) {
            return ResponseEntity.badRequest().body(new MessageResponse("Profile already exists! Update it instead."));
        }

        profileService.createProfile(profileRequest, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Profile created successfully!"));
    }

    @PatchMapping
    public ResponseEntity<?> doUpdateProfile(@Valid @RequestBody ProfileRequest profileRequest) {

        User user = userDetailsService.getAuthenticatedUser();
        Profile profile = profileService.findByUserIdEntity(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Profile Not Found with user id: " + user.getId()));

        profileService.updateProfile(profile, profileRequest);

        return ResponseEntity.ok().body(new MessageResponse("Profile updated successfully!"));

    }
}
