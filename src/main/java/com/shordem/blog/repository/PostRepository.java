package com.shordem.blog.repository;

import java.util.Optional;
import java.util.UUID;

import com.shordem.blog.entity.Post;

public interface PostRepository extends BaseRepository<Post, UUID> {

    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
