package com.shordem.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shordem.blog.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findBySlug(String slug);
}