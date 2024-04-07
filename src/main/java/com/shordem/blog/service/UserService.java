package com.shordem.blog.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shordem.blog.entity.User;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService extends BaseService<User> {
    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new EntityNotFoundException(email + " not found"));
    }

    public Boolean userIsVerified(String email) {
        return findByEmail(email).getIsEmailVerified();
    }

    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }
}
