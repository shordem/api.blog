package com.shordem.blog.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shordem.blog.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findBySlug(String slug);

    List<Tag> findAllByOrderByCreatedAtDesc();

    Set<Tag> findAllBySlugIn(String[] tags);
}