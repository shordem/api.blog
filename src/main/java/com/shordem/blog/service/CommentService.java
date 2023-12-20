package com.shordem.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shordem.blog.dto.CommentDto;
import com.shordem.blog.entity.Comment;
import com.shordem.blog.repository.CommentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService extends BaseService<Comment> {

    private final CommentRepository commentRepository;

    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(comment.getContent(), comment.getCreatedBy().getUsername());
    }

    @Override
    public void save(Comment entity) {
        commentRepository.save(entity);
    }

    public List<CommentDto> findAllByPostSlug(String slug) {
        List<Comment> comment = commentRepository.findAllByPostSlug(slug);

        return comment.stream().map(this::convertToDto).toList();
    }

}
