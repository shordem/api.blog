package com.shordem.blog.service;

import org.springframework.stereotype.Service;

import com.shordem.blog.entity.Tag;
import com.shordem.blog.repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Boolean existsBySlug(String slug) {
        return tagRepository.findBySlug(slug).isPresent();
    }

    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    public Tag findBySlug(String slug) {
        return tagRepository.findBySlug(slug).orElse(null);
    }

    public Iterable<Tag> findAll() {
        return tagRepository.findAll();
    }
}
