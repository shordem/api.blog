package com.shordem.blog.service;

import org.springframework.stereotype.Service;

import com.shordem.blog.entity.Like;
import com.shordem.blog.repository.LikeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService extends BaseService<Like> {

    private final LikeRepository likeRepository;

    @Override
    public void save(Like entity) {
        likeRepository.save(entity);
    }
}
