package com.shordem.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shordem.blog.entity.Profile;
import com.shordem.blog.entity.User;
import com.shordem.blog.payload.response.MessageResponse;
import com.shordem.blog.service.AwsS3Service;
import com.shordem.blog.service.ProfileService;
import com.shordem.blog.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile/")
public class ProfileController {

    @Autowired
    private AwsS3Service awsS3Service;

    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        Profile profile = profileService.findByUserId(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Profile Not Found with user id: " + user.getId()));

        return ResponseEntity.ok().body(profile);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doCreateProfile(@RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("bio") String bio,
            @RequestParam("website") String website,
            @RequestParam("mail") String mail,
            @RequestParam("x") String x,
            @RequestParam("facebook") String facebook,
            @RequestParam("instagram") String instagram,
            @RequestParam("avatar") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        Boolean profileCheck = profileService.existsByUserId(user.getId());

        if (profileCheck) {
            return ResponseEntity.badRequest().body(new MessageResponse("Profile already exists! Update it instead."));
        }

        String fileKey = awsS3Service.uploadFile(file);

        Profile profile = new Profile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setBio(bio);
        profile.setAvatar(fileKey);
        profile.setWebsite(website);
        profile.setMail(mail);
        profile.setX(x);
        profile.setFacebook(facebook);
        profile.setInstagram(instagram);
        profile.setUser(user);

        profileService.saveProfile(profile);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Profile created successfully!"));
    }
}
