package com.shordem.blog.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shordem.blog.entity.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, UUID> {

    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Post save(Post post);
}
