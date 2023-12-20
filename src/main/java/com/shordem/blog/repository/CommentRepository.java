package com.shordem.blog.repository;

import java.util.List;
import java.util.UUID;

import com.shordem.blog.entity.Comment;

public interface CommentRepository extends BaseRepository<Comment, UUID> {

    List<Comment> findAllByPostSlug(String slug);
}
