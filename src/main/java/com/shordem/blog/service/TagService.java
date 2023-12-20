package com.shordem.blog.service;

import org.springframework.stereotype.Service;

import com.shordem.blog.entity.Tag;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService extends BaseService<Tag> {

    private final TagRepository tagRepository;

    private TagDto convertToDto(Tag tag) {
        return new TagDto(tag.getName(), tag.getSlug());
    }

    public Boolean existsBySlug(String slug) {
        return tagRepository.findBySlug(slug).isPresent();
    }

    public void save(Tag entity) {
        tagRepository.save(entity);
    }

    public Tag findBySlug(String slug) {
        return tagRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException());
    }

    public Iterable<Tag> findAll() {
        return tagRepository.findAll();
    }
}
